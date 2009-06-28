package uk.co.danielrendall.fractdim;

import org.bs.mdi.Document;
import org.bs.mdi.Application;
import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.app.FDData;
import uk.co.danielrendall.fractdim.app.FDView;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.workers.CalculateStatisticsWorker;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
import uk.co.danielrendall.fractdim.app.datamodel.CompoundDataModel;
import uk.co.danielrendall.fractdim.app.datamodel.ModelStatusListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Jun-2009
 * Time: 15:47:11
 * To change this template use File | Settings | File Templates.
 */
public class FDDocument extends Document {


    private final CompoundDataModel settingsModel = new CompoundDataModel(CalculationSettings.class);
    private final Set<ModelStatusListener> modelStatusListeners;

    public FDDocument(Application app) {
        super(app);
        modelStatusListeners = new HashSet<ModelStatusListener>();
        settingsModel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (CompoundDataModel.MODEL_VALIDITY.equals(evt.getPropertyName())) {
                    if ((Boolean) evt.getNewValue()) {
                        synchronized(modelStatusListeners) {
                            for(ModelStatusListener listener : modelStatusListeners) {
                                listener.modelIsGood();
                            }
                        }
                    } else {
                        synchronized(modelStatusListeners) {
                            for(ModelStatusListener listener : modelStatusListeners) {
                                listener.modelIsBad();
                            }
                        }
                    }
                }
            }

        });

    }

    // post creation initialization when views and data have been set up
    public void init() {
        final FDData data = ((FDData) getData());
        addModelStatusListener(new ModelStatusListener() {
            public void modelIsGood() {
                data.setSettings((CalculationSettings)settingsModel.getNewModel());

            }

            public void modelIsBad() {
                // ignore
            }
        });


        FDView view = (FDView) getView(0);
        view.setSettingsModel(settingsModel);
        addWorker(new CalculateStatisticsWorker(this));
    }


    public void setStatistics(Statistics statistics) {
        FDData data = ((FDData) getData());
        data.setStatistics(statistics);

        CalculationSettings initialSettings = new CalculationSettings(statistics);

        data.setSettings(initialSettings);

        FDView view = (FDView) getView(0);
        view.syncWithData();
    }

    public void setSquareCountingResult(SquareCountingResult squareCountingResult) {
        //To change body of created methods use File | Settings | File Templates.
    }


    public void addModelStatusListener(ModelStatusListener listener) {
        synchronized(modelStatusListeners) {
            modelStatusListeners.add(listener);
        }
    }

    public void removeModelStatusListener(ModelStatusListener listener) {
        synchronized(modelStatusListeners) {
            modelStatusListeners.remove(listener);
        }
    }


}
