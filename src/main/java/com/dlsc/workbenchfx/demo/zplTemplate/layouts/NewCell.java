package com.dlsc.workbenchfx.demo.zplTemplate.layouts;

import com.cg.core.zpl.Cell;

class NewCell extends Cell {
    public NewCell(float left, float top, float width, float height) {
        super(left,  top,  left+width,  top+height);
    }
    public float getHeight() {
        return this.getBottom()-this.getTop();
    }
    public float getWidth() {
        return this.getRight()-this.getLeft();
    }
}