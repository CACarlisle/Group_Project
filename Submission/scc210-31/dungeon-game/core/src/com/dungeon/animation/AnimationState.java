package com.dungeon.animation;

public class AnimationState {
    public String name;
    public Animator animator;
    public boolean switchBackAfterPlaying;
    
    public AnimationState(String name, Animator animator, boolean switchBackAfterPlaying) {
        this.name = name;
        this.animator = animator;
        this.switchBackAfterPlaying = switchBackAfterPlaying;
    }
}