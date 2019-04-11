package com.example.athena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Array;

public class DialogStage extends Stage {

    //Status ob der Dialog gerade sichtbar ist
    private boolean isVisible;

    //Eine Liste mit Texten, die angezeigt werden sollen
    private Array<String> texts = new Array<String>();

    //Das UI Element, das die Texte anzeigt.
    private TextArea area;

    //Erstellt die Dialogstage mit einer Höhe von 100px und maximaler Breite
    public DialogStage(final Skin skin) {
        area = new TextArea("#", skin);
        area.setWidth(Gdx.graphics.getWidth());
        area.setHeight(100);
        addActor(area);
    }

    //Setzt die Texte die angezeigt werden sollen.
    public void setTexts(String... texts) {
        this.texts.clear();
        this.texts.addAll(texts);
        next();
    }


    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }

    public void resize(final int width, final int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void draw() {
        if (isVisible) {
            super.draw();
        }
    }

    @Override
    public void act() {
        if (isVisible) {
            super.act();
        }
    }

    @Override
    public void act(final float delta) {
        if (isVisible) {
            super.act(delta);
        }
    }

    @Override
    public boolean keyUp(final int keyCode) {
        if (!isVisible) {
            return false;
        }
        if (keyCode == Input.Keys.ENTER) {
            if (texts.size > 0) {
                //set next dialog text
                next();
            } else {
                //hide dialog area cause there is no more text to show.
                hide();
            }
        }
        return true;
    }

    @Override
    public boolean keyDown(final int keyCode) {
        return isVisible;
    }

    private void next() {
        area.clear();
        area.setText(texts.first());
        texts.removeIndex(0);
    }
}
