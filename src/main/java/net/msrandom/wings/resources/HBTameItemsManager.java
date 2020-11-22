package net.msrandom.wings.resources;

import com.google.gson.stream.JsonReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.msrandom.wings.WingsAndClaws;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class HBTameItemsManager implements ISelectiveResourceReloadListener {
    private static final ResourceLocation LOCATION = new ResourceLocation(WingsAndClaws.MOD_ID, "tame_items/hatchet_beak.json");
    private final Object2IntMap<Item> points = new Object2IntOpenHashMap<>();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        try (IResource resource = resourceManager.getResource(LOCATION); JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()))) {
            reader.beginObject();
            points.clear();
            while (reader.hasNext()) {
                String name = reader.nextName();
                int value = reader.nextInt();
                if (name.startsWith("#")) {
                    ITag<Item> items = ItemTags.getCollection().get(new ResourceLocation(name.replace("#", "")));
                    if (items != null) {
                        for (Item element : items.getAllElements()) {
                            points.put(element, value);
                        }
                    }
                } else {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
                    if (item != null && item != Items.AIR) {
                        points.put(item, value);
                    }
                }
            }
            reader.endObject();
        } catch (IOException e) {
            WingsAndClaws.LOGGER.error("Failed to read Hatchet Beak item tame points", e);
        }
    }

    public int getPoints(Item item) {
        return points.getOrDefault(item, 0);
    }
}
