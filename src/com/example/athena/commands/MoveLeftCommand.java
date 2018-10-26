package com.example.athena.commands;

import com.example.athena.model.Entity;

public class MoveLeftCommand implements Command {
    @Override
    public void execute(final Entity entity) {
        entity.setX(entity.getX() - 1);
    }
}
