package com.dlsc.workbenchfx.demo.modules.query.model;

import javafx.scene.Node;

public class GridField{
    public Node node;
    public int width;

    public static GridField OfNode(Node node){
        GridField field = new GridField();
        field.node = node;
        return field;
    }
    public GridField span(int width){
        this.width = width;
        return this;
    }

}

