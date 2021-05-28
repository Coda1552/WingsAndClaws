package coda.wingsandclaws.resources;

import coda.wingsandclaws.WingsAndClaws;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TamePointsManager extends JsonReloadListener {
    public static final TamePointsManager INSTANCE = new TamePointsManager();

    private final Map<EntityType<?>, Object2IntMap<Item>> points = new HashMap<>();

    public TamePointsManager() {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), "tame_items");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager manager, IProfiler profilerIn) {
        points.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation id = entry.getKey();
            try (IResource resource = manager.getResource(getPreparedPath(id)); JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()))) {
                reader.beginObject();
                Object2IntMap<Item> pointsFromItem = new Object2IntOpenHashMap<>();

                while (reader.hasNext()) {
                    String name = reader.nextName();
                    int value = reader.nextInt();
                    if (name.startsWith("#")) {
                        ITag<Item> items = ItemTags.getCollection().get(new ResourceLocation(name.replace("#", "")));
                        if (items != null) {
                            for (Item element : items.getAllElements()) {
                                pointsFromItem.put(element, value);
                            }
                        }
                    } else {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
                        if (item != null && item != Items.AIR) {
                            pointsFromItem.put(item, value);
                        }
                    }
                }
                points.put(ForgeRegistries.ENTITIES.getValue(id), pointsFromItem);
            } catch (Exception e) {
                WingsAndClaws.LOGGER.error("Unable to parse tame points for {}", id, e);
            }
        }
    }

    public int getPoints(EntityType<?> type, Item item) {
        return points.containsKey(type) ? points.get(type).getOrDefault(item, 0) : 0;
    }
}
