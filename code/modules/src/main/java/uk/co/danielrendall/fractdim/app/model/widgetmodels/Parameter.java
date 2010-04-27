package uk.co.danielrendall.fractdim.app.model.widgetmodels;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 25-Apr-2010
 * Time: 12:00:52
 * To change this template use File | Settings | File Templates.
 */
public class Parameter {

    private final String namespace;
    private final String id;
    private final String name;
    private final String description;

    public Parameter(String namespace, String id, String name, String description) {
        this.namespace = namespace;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter parameter = (Parameter) o;

        if (id != null ? !id.equals(parameter.id) : parameter.id != null) return false;
        if (namespace != null ? !namespace.equals(parameter.namespace) : parameter.namespace != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = namespace != null ? namespace.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
