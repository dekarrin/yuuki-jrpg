package yuuki.content;

import java.util.List;
import java.util.Map;

import yuuki.action.ActionFactory;
import yuuki.entity.EntityFactory;
import yuuki.graphic.ImageFactory;
import yuuki.sound.DualSoundEngine;
import yuuki.world.PortalFactory;
import yuuki.world.TileFactory;

/**
 * Container class to hold all items that are a part of game engine content.
 */
class Content {
	
	public ActionFactory actions = null;
	public EntityFactory entities = null;
	public ImageFactory images = null;
	public DualSoundEngine sounds = new DualSoundEngine();
	public PortalFactory portals = null;
	public TileFactory tiles = null;
	public Map<String, String> imagePaths = null;
	public Map<String, String> effectPaths = null;
	public Map<String, String> musicPaths = null;
	public List<String> landPaths = null;
}
