package uk.co.danielrendall.fractdim.app.datamodel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.awt.*;

import uk.co.danielrendall.fractdim.logging.Log;


/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 14-Jun-2009
 * Time: 15:44:27
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class CompoundDataModel {

    private final Class dataModelClass;
    private final List<String> fieldNames = new ArrayList<String>();
    private final Map<String, Class> fieldTypes = new HashMap<String, Class>();
    private final Map<String, Boolean> fieldIsValid = new HashMap<String, Boolean>();
    private final Map<String, Object> currentValues = new HashMap<String, Object>();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String FIELD_VALIDITY = "FIELD_VALIDITY";
    public static final String MODEL_VALIDITY = "MODEL_VALIDITY";
    
    public CompoundDataModel(Class dataModelClass) {
        this.dataModelClass  = dataModelClass;
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(dataModelClass);

        for (PropertyDescriptor pd : pds) {
            if (pd.getPropertyType() != Class.class) {
                final String fieldName = pd.getName();
                fieldTypes.put(fieldName, pd.getPropertyType());
                currentValues.put(fieldName, null);
                fieldIsValid.put(fieldName, false);
                fieldNames.add(fieldName);
            }
        }
    }

    public Object getNewModel() {
        try {
            Object newModel = dataModelClass.newInstance();
            BeanUtils.populate(newModel, currentValues);
            return newModel;
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void bind(String propertyName, JTextComponent tc) {
        Class fieldType = fieldTypes.get(propertyName);
        if (fieldType == null) {
            throw new IllegalArgumentException("Property " + propertyName + " doesn't exist in the model");
        }

        tc.getDocument().addDocumentListener(new ComponentListener(tc, propertyName, getConverter(fieldType), new ComponentValueExtractor(){
            public Object getValue(JComponent component) {
                Log.gui.debug("Got value '" + ((JTextComponent) component).getText() + "'");                
                return ((JTextComponent) component).getText().trim();
            }
        }));
    }

    public void bind(String propertyName, JSpinner spn) {
        Class fieldType = fieldTypes.get(propertyName);
        if (fieldType == null) {
            throw new IllegalArgumentException("Property " + propertyName + " doesn't exist in the model");
        }

        spn.addChangeListener(new ComponentListener(spn, propertyName, getConverter(fieldType), new ComponentValueExtractor(){
            public Object getValue(JComponent component) {
                return ((JSpinner) component).getValue();
            }
        }));
    }

    public void bind(String propertyName, JList list) {
        Class fieldType = fieldTypes.get(propertyName);
        if (fieldType == null) {
            throw new IllegalArgumentException("Property " + propertyName + " doesn't exist in the model");
        }

        list.addListSelectionListener(new ComponentListener(list, propertyName, getConverter(fieldType), new ComponentValueExtractor(){
            public Object getValue(JComponent component) {
                Log.gui.debug("Got value '" + ((JList) component).getSelectedValue().toString().trim() + "'");
                return ((JList) component).getSelectedValue().toString().trim();
            }
        }));
    }

    public boolean modelIsValid() {
        for(boolean valid : fieldIsValid.values()) {
            if (!valid) return false;
        }
        return true;
    }

    private Converter getConverter(Class clazz) {
        if (clazz == double.class) {
            return new DoubleConverter();
        } else if (clazz == int.class) {
            return new IntegerConverter();
        } else if (clazz == String.class) {
            return new StringConverter();
        } else {
            return new Converter() {
                public Object convert(Object value) throws Exception {
                    return value.toString();
                }
            };
        }
    }

    private class ComponentListener implements DocumentListener, ChangeListener, ListSelectionListener {

        protected final JComponent component;
        private final String propertyName;
        private final Converter converter;
        private final ComponentValueExtractor extractor;
        private final int index;

        public ComponentListener(JComponent component, String propertyName, Converter converter, ComponentValueExtractor extractor) {
            this.component = component;
            this.propertyName = propertyName;
            this.converter = converter;
            this.extractor = extractor;
            index = fieldNames.indexOf(propertyName);
        }

        protected void handleChange() {
            boolean modelIsInitallyValid = modelIsValid();
            boolean fieldIsInitiallyValid = fieldIsValid.get(propertyName);
            try {
                Object newValue = converter.convert(extractor.getValue(component));
                currentValues.put(propertyName, newValue);
                fieldIsValid.put(propertyName, true);
                component.setBackground(Color.WHITE);
                if (!fieldIsInitiallyValid) pcs.fireIndexedPropertyChange(FIELD_VALIDITY, index, false, true);
            } catch (Exception e) {
                Log.misc.debug(e.getMessage());
                fieldIsValid.put(propertyName, false);
                if (fieldIsInitiallyValid) pcs.fireIndexedPropertyChange(FIELD_VALIDITY, index, true, false);
                component.setBackground(Color.RED);
            }
            boolean modelIsNowValid = modelIsValid();
//            pcs.firePropertyChange(MODEL_VALIDITY, modelIsInitallyValid, modelIsNowValid);
            pcs.firePropertyChange(MODEL_VALIDITY, false, modelIsNowValid);
        }

        public void insertUpdate(DocumentEvent e) {
            handleChange();
        }

        public void removeUpdate(DocumentEvent e) {
            handleChange();
        }

        public void changedUpdate(DocumentEvent e) {
            handleChange();
        }

        public void stateChanged(ChangeEvent e) {
            handleChange();
        }

        public void valueChanged(ListSelectionEvent e) {
            handleChange();
        }
    }

    private static interface ComponentValueExtractor {
        public Object getValue(JComponent component);
    }

    private static interface Converter {
        public Object convert(Object value) throws Exception;
    }

    private static class DoubleConverter implements Converter {
        public Object convert(Object value) throws Exception {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                throw new Exception(e);
            }
        }
    }

    private static class IntegerConverter implements Converter {
        public Object convert(Object value) throws Exception {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                throw new Exception(e);
            }
        }
    }

    private static class StringConverter implements Converter {
        public Object convert(Object value) throws Exception {
            return value.toString();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
