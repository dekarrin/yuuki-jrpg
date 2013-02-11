/**
 * Provides classes for driving animation. Most clients' needs can be satisfied
 * with the use of {@link yuuki.animation.engine.AnimationManager}, which
 * manages at least one {@link yuuki.animation.engine.AnimationDriver}, each of
 * which drives several {@link yuuki.animation.engine.Animatable} instances.
 * 
 * Any class that implements {@link yuuki.animation.engine.Animatable} may be
 * animated by this system.
 */
package yuuki.animation.engine;