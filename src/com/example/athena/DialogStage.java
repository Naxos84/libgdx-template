package com.example.athena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Array;
import com.example.athena.manager.events.Event;
import com.example.athena.manager.events.EventType;

public class DialogStage extends Stage implements com.example.athena.manager.events.EventListener{

    private boolean isVisible;

    private Array<String> texts = new Array<String>();
    private TextArea area;

    public DialogStage(final Skin skin) {
        area = new TextArea("#", skin);
        area.setWidth(Gdx.graphics.getWidth());
        area.setHeight(100);
        addActor(area);
    }

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

    private void next() {
        area.clear();
        area.setText(texts.first());
        texts.removeIndex(0);
    }

    @Override
    public void executeEvent(final Event e) {
        if (e.getType() == EventType.DIALOG) {
            setTexts((String[])e.getSource());
            show();
        }
    }
}
