/**
 * <p>
 * Provides animations for use with {@link yuuki.sprite.Sprite} and sub-classes
 * located in the {@link yuuki.sprite} package. All animations are designed to
 * be driven by an instance of {@link yuuki.animation.engine.AnimationDriver},
 * and for that reason each concrete class in {@code yuuki.animation}
 * implements the methods specified by
 * {@link yuuki.animation.engine.Animatable}, either through sub-classing
 * {@code Animation} or by overriding the methods themselves.
 * </p><p>
 * All classes in {@code yuuki.animation} contain a reference to the
 * {@code Sprite} being animated, located in the {@code sprite} property. Some
 * sub-classes of {@code Animation} may choose to narrow the type of this
 * reference in order to provide more specific behavior.
 * {@link yuuki.animation.TextTween} is an example of this in the existing API;
 * it narrows {@code sprite}'s type to {@link yuuki.ui.MessageBox} so that it
 * may use the text-setting methods of {@code MessageBox}.
 * </p><p>
 * <h3>Animation Hierarchy</h3>
 * <img src="doc-files/hierarchy.png" />
 * </p><p>
 * The {@link yuuki.animation.Animation} class is the root of the class
 * hierarchy. It implements many of the methods in {@code Animatable}, though
 * sub-classes may extend their behavior. {@code Animation} itself is abstract,
 * and sub-classes must implement three methods called from {@code Animation}:
 * <ul>
 * <li>{@link yuuki.animation.Animation#advance(int) advance(int)} advances
 * animation by one frame at the given frame rate. Sub-classes must implement
 * this method to do whatever behavior is specific to the animation that each
 * sub-class represents.</li>
 * <li>{@link yuuki.animation.Animation#isAtEnd() isAtEnd()} determines when
 * the animation is complete. Some animations never complete unless explicitly
 * stopped, and thus always return false. Any animation that does have a
 * clearly-defined stopping point should return true when it reaches that point
 * and false if it has not reached that point.</li>
 * <li>{@link yuuki.animation.Animation#resetProperties() resetProperties()}
 * causes the state of the animation to be reset to what it was when it was
 * created. Sub-classes must use this method to set their properties to the
 * values that they had after the constructor was called.</li>
 * </ul>
 * </p><p>
 * The {@link yuuki.animation.Advancement} class calls
 * {@link yuuki.animation.engine.Animatable#advanceFrame(int) advanceFrame} one
 * time on the animated {@code Sprite}. {@code Advancement} is typically used
 * as the looped animation in {@link yuuki.animation.Loop} in order to allow
 * the animated {@code Sprite} to define its own animation behavior.
 */
package yuuki.animation;