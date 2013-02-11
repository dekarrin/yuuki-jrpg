/**
 * Provides in-game intelligent agent functionality.
 * 
 * {@link yuuki.entity.PlayerCharacter} and
 * {@link yuuki.entity.NonPlayerCharacter} are the main types of
 * {@link yuuki.entity.Character} classes, and contain the actual entity
 * functionality.
 * 
 * {@link yuuki.entity.ActionFactory} and {@link yuuki.entity.EntityFactory}
 * are used for creating Character instances. Unless very specific behavior is
 * desired, EntityFactory should always be used to generate instances rather
 * than calling the constructor of either PlayerCharacter or
 * NonPlayerCharacter.
 * 
 * {@link yuuki.entity.VariableStat} and {@link yuuki.entity.Stat} together
 * make up the framework for a Character's stats.
 */
package yuuki.entity;