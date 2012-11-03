package forge;

import java.util.Set;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.Entity;
import net.minecraft.server.World;

/**
 * Register an implementation of this interface to be used for chunk load handling.
 */
public interface IChunkLoadHandler
{
    /**
     * Called from World.tickBlocksAndAmbiance
     * Add loaded chunks to this set for them to receive block tick updates
     * @param world The world containing the chunks
     * @param chunkList The set of active chunks
     */
    public void addActiveChunks(World world, Set<ChunkCoordIntPair> chunkList);

    /**
     * Called from ChunkProvider.dropChunk
     * Return false to prevent the unloading of this chunk
     * @param chunk The chunk to be unloaded
     */
    public boolean canUnloadChunk(Chunk chunk);
    
    /**
     * Called from World.updateEntityWithOptionalForce.
     * Return true to allow this entity to update.
     * @param entity The entity to update
     */
    public boolean canUpdateEntity(Entity entity);
}