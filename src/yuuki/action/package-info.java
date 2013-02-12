/**
 * Contains classes for Yuuki's action system. All classes inherit from
 * {@link yuuki.action.Action}, which provides basic functionality, but users
 * may find it more useful to inherit from {@link yuuki.action.Skill}.
 * <p>
 * {@link yuuki.action.Flee} is a special subclass of {@code Action} whose
 * use is explicitly tested for by {@link yuuki.battle.Battle}.
 * <p>
 * Subclasses of {@code Action} have several responsibilities. They must set
 * their actual effects, as well as their cost and effect stats.
 * <p>
 * Note that any actions added need to be added to the
 * {@link yuuki.entity.ActionFactory} class' action base definitions list.
 */
package yuuki.action;