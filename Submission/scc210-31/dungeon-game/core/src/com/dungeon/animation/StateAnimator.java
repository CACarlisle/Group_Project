package com.dungeon.animation;

import java.util.ArrayList;
import com.dungeon.debug.*;
import com.dungeon.entities.Entity;

public class StateAnimator {
    private Entity entity;
    private ArrayList<AnimationState> states;

    private AnimationState current;
    private AnimationState defaultState;
    
    public StateAnimator(Entity entity) {
        this.entity = entity;
        states = new ArrayList<AnimationState>();
    }

    public void addState(String stateName, Animator anim, boolean switchBackAfterPlaying) {
        AnimationState newState = new AnimationState(stateName, anim, switchBackAfterPlaying);
        states.add(newState);

        if (defaultState == null) {
            setDefault(stateName);
        }
    }

    public void setStateAnimator(String stateName, Animator newAnimator) {
        AnimationState temp = getState(stateName);

        if (temp == null) return;

        temp.animator = newAnimator;
    }

    public void setDefault(String stateName) {
        AnimationState temp = getState(stateName);
        
        if (temp == null) return;

        defaultState = temp;
        if (current == null) current = temp;
    }

    public void switchState(String stateName) {
        AnimationState temp = getState(stateName);

        if (temp == null) return;

        if (temp != current) {
            current.animator.reset();
            current = temp;
        } 
    }

    public AnimationState getState(String stateName)
    {
        for (AnimationState a : states) {
            if (a.name.equals(stateName)) {
                return a;
            }
        }

        Debugger.logWarning("Animation State '" + stateName + "' does not exist.");
        return null;
    }

    public void animate()
    {
        if (current == null) return;
        if (current.animator == null) return;

        current.animator.animate(entity);

        if (current.animator.isFininshed() && current.switchBackAfterPlaying){
            current.animator.reset();
            current = defaultState;
        } 
    }
}
