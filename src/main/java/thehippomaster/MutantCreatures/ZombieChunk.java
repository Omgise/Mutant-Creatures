package thehippomaster.MutantCreatures;

public class ZombieChunk {
   public int posX;
   public int posY;
   public int posZ;
   public boolean first;
   public boolean spawnParticles;

   public ZombieChunk(int x, int y, int z) {
      this.posX = x;
      this.posY = y;
      this.posZ = z;
      this.first = false;
      this.spawnParticles = true;
   }

   public ZombieChunk setFirst(boolean flag) {
      this.first = flag;
      return this;
   }

   public ZombieChunk setParticles(boolean flag) {
      this.spawnParticles = flag;
      return this;
   }
}
