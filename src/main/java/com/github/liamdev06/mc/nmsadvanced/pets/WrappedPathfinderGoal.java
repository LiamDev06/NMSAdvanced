package com.github.liamdev06.mc.nmsadvanced.pets;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

/**
 * Wrapped pathfinder goal to wrap obfuscated NMS methods into understandable ones
 */
public abstract class WrappedPathfinderGoal extends PathfinderGoal {

    /**
     * The NMS entity that this pathfinder goal is navigating/controlling
     */
    private final EntityInsentient navigatedEntity;

    public WrappedPathfinderGoal(EntityInsentient navigatedEntity) {
        this.navigatedEntity = navigatedEntity;
    }

    /**
     * @return the entity this pathfinder goal is navigating/controlling
     */
    public EntityInsentient getNavigatedEntity() {
        return this.navigatedEntity;
    }

    /**
     * Called every single tick
     * @return if the pathfinder goal can navigate the controlled entity
     */
    protected abstract boolean canNavigate();

    /**
     * Called when {@link #canNavigate()} is true
     */
    protected abstract void onNavigationTick();

    /**
     * Checks every tick if we can continue calling {@link #canNavigate()}
     * @return if we can continue calling {@link #canNavigate()}
     */
    protected abstract boolean canContinueNavigation();

    /**
     * Executed if {@link #canContinueNavigation()} returns false
     */
    protected abstract void onNavigationStop();

    /**
     * Obfuscated method for {@link #canNavigate()}
     */
    @Override
    public final boolean a() {
        return this.canNavigate();
    }

    /**
     * Obfuscated method for {@link #canContinueNavigation()}
     */
    @Override
    public final boolean b() {
        return super.b() && this.canContinueNavigation();
    }

    /**
     * Obfuscated method for {@link #onNavigationTick()}
     */
    @Override
    public final void c() {
        super.c();
        this.onNavigationTick();
    }

    /**
     * Obfuscated method for {@link #onNavigationStop()}
     */
    @Override
    public final void d() {
        super.d();
        this.onNavigationStop();
    }

    /**
     * Navigate {@link #navigatedEntity} to the location coordinates provided at
     * the speed provided
     *
     * @param locX location x coordinate to move to
     * @param locY location y coordinate to move to
     * @param locZ location z coordinate to move to
     * @param speed the speed to move at
     * @author Liam
     */
    public void navigateTo(double locX, double locY, double locZ, double speed) {
        this.navigatedEntity.getNavigation().a(locX, locY, locZ, speed);
    }
}
