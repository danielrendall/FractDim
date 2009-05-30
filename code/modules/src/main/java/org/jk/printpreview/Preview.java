/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
 *
 *  Written by: 2003 Jens Kaiser <jens.kaiser@web.de>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.jk.printpreview;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;

class Preview extends JComponent {
	private final static int DEFAULT_PREVIEW_SIZE = 300;
	private final static double MINIMUM_ZOOM_FACTOR = 0.1;
	private final static int SHADOW_WIDTH = 6;
	private final static Color SHADOW_COLOR = Color.GRAY;

	public Preview(Pageable pageable, double zoom) {
		this.pageable = pageable;
		PageFormat format = pageable.getPageFormat(index);
		if (zoom == 0.0) {
			if (format.getOrientation() == PageFormat.PORTRAIT)
				this.zoom = DEFAULT_PREVIEW_SIZE / format.getHeight();
			else
				this.zoom = DEFAULT_PREVIEW_SIZE / format.getWidth();
		} else
			this.zoom = zoom;
		resize();

		MouseInputAdapter mia = new MouseInputAdapter() {
			int m_XDifference;
			int m_YDifference;
			Container c;

			public void mouseDragged(MouseEvent e) {
				JViewport jv = (JViewport)Preview.this.getParent().getParent().getParent();
				Point p = jv.getViewPosition();			
				int newX = p.x - (e.getX() - m_XDifference);				
				int maxX = Preview.this.getParent().getParent().getWidth() - jv.getWidth();	
				int newY = p.y - (e.getY() - m_YDifference);				
				int maxY = Preview.this.getParent().getParent().getHeight() - jv.getHeight();

				if (newX < 0) {newX = 0; m_XDifference = e.getX();} 
				if (newX > maxX) {newX = maxX; m_XDifference = e.getX();}								
				if (newY < 0) {newY = 0; m_YDifference = e.getY();} 
				if (newY > maxY) {newY = maxY; m_YDifference = e.getY();}
				jv.setViewPosition(new Point(newX, newY));
			}

			public void mousePressed(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				m_XDifference = e.getX();
				m_YDifference = e.getY();				
			}

			public void mouseReleased(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		};

		addMouseMotionListener(mia);
		addMouseListener(mia);

	}
    
    protected void paintPaper(Graphics g, PageFormat format) {
        g.setColor(Color.white);        
        g.fillRect(0, 0, (int)format.getWidth(), (int)format.getHeight());        
        g.setColor(Color.black);
        g.drawRect(0, 0, (int)format.getWidth() - 1, (int)format.getHeight() - 1);        
    }
    
    public void paintComponent(Graphics g) {
        try {			
        	Dimension size = getSize();
            PageFormat format = pageable.getPageFormat(index);   
            Printable printable = pageable.getPrintable(index);
			g.setColor(SHADOW_COLOR);
			g.fillRect(SHADOW_WIDTH, SHADOW_WIDTH, 
				size.width - SHADOW_WIDTH, size.height - SHADOW_WIDTH);
			Graphics2D g2d = (Graphics2D)g;
			g2d.scale(zoom, zoom);						
            paintPaper(g, format);
            printable.print(g, format, 0);
        } catch (PrinterException e) {
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void movePageIndex(int indexStep) {
        int newIndex = index + indexStep;
        setPageIndex(newIndex);
    }
    
    
    public int getMaxPageIndex() {
    	return pageable.getNumberOfPages() - 1;
    }
    
    public int getPageIndex() {
    	return index;
    }
    
    public void setPageIndex(int newIndex) {
    	if (newIndex < 0) return;
    	if (newIndex > getMaxPageIndex()) return;
		try {			
			Printable printable = pageable.getPrintable(newIndex);
			resize();
			index = newIndex;
			repaint();
		} catch (IndexOutOfBoundsException ignored) {
		}    	
    }
    
    public void changeZoom(double zoom) {
        this.zoom = Math.max(MINIMUM_ZOOM_FACTOR, this.zoom + zoom);
        resize();
        center();
    }
    
    public void resize() {
    	Rectangle oldbounds = getBounds();
        PageFormat format = pageable.getPageFormat(index);
        int width = (int)(format.getWidth() * zoom)+SHADOW_WIDTH;
        int height = (int)(format.getHeight() * zoom)+SHADOW_WIDTH;
        int dw = width - oldbounds.width;
        int dh = height - oldbounds.height;
        setPreferredSize(new Dimension(width, height));        
        setBounds(oldbounds.x - dw/2, oldbounds.y - dh/2, width, height);        		
        revalidate();
    }
    
    public void center() {
		Dimension tsize = getParent().getSize();
		Dimension psize = getSize();
		setLocation(tsize.width/2 - psize.width/2, tsize.height/2 - psize.height/2);
    }
    
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
    
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
    
    protected Pageable pageable;
    protected int index = 0;
    protected double zoom = 0.0;
}
