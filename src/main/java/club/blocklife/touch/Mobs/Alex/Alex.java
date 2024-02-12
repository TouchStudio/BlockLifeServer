package club.blocklife.touch.Mobs.Alex;

import club.blocklife.touch.SkyblockMobs;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;
import java.util.Random;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 21:57
 * @Tips XuFang is Gay!
 */
public class Alex implements Listener {
    public static Alex instance;
    private Random random = new Random();
    public static void onStart(Location location){
    Blaze alex = Bukkit.getWorld("world").spawn(location, Blaze.class);
    location.getWorld().spawnParticle(Particle.FLAME, location, 100);
    alex.setMetadata("Alex", new FixedMetadataValue(SkyblockMobs.instance, "Alex"));
    location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
    alex.setCustomName("Alex");
    alex.setCustomNameVisible(true);
    alex.setAI(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                // 检查史莱姆是否还活着
                if (!alex.isValid()) {
                    this.cancel(); // 如果史莱姆死了，取消定时任务
                    return;
                }

                // 在史莱姆周围生成4只僵尸
                for (int i = 0; i < 4; i++) {
                    Location spawnLocation = alex.getLocation().clone().add(
                            Math.random() * 10 - 5, // X轴随机偏移
                            0, // Y轴不变
                            Math.random() * 10 - 5); // Z轴随机偏移
                    Blaze entourage = (Blaze) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.BLAZE);
                    spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 100);
                    spawnLocation.getWorld().playSound(spawnLocation, "entity.blaze.ambient", 1.0F, 1.0F);
                    entourage.setCustomName("Alex's Entourage");
                    entourage.setCustomNameVisible(true);
                    entourage.damage(2.5);
                    entourage.setAI(true);
                    entourage.setMetadata("Alex's Entourage", new FixedMetadataValue(SkyblockMobs.instance, "Alex's Entourage"));
                    entourage.setTarget(instance.findNearestPlayer(spawnLocation));
                }
            }
        }.runTaskTimer(SkyblockMobs.instance, 0L, 130L); // 0L是初始延迟，100L是周期（20L等于1秒）
    }

    private Player findNearestPlayer(Location location) {
        double nearestDistanceSquared = Double.MAX_VALUE;
        Player nearestPlayer = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            double distanceSquared = player.getLocation().distanceSquared(location);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // 检查死亡的实体是否是僵尸
        if (event.getEntity().hasMetadata("Alex's Entourage")) {
            event.getDrops().clear();
            double dropChance = 0.10; // 设置掉落概率为25%
            if (random.nextDouble() < dropChance) { // 使用Random对象生成一个0到1之间的随机数，并与掉落概率比较
                // 如果随机数小于掉落概率，则添加自定义掉落物
                ItemStack customDrop = new ItemStack(Material.LAVA_BUCKET, 1);
                event.getDrops().add(customDrop);
            }
        }else if (event.getEntity().hasMetadata("Alex")) {
            event.getDrops().clear();
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().hasMetadata("Alex")) {
            // 检查是否存在活着的关键实体
            boolean keyEntityAlive = false;
            for (Entity entity : event.getEntity().getWorld().getEntities()) {
                if (entity.hasMetadata("Alex's Entourage") && !entity.isDead()) {
                    keyEntityAlive = true;
                    break;
                }
            }

            // 如果关键实体还活着，取消对受保护实体的伤害
            if (keyEntityAlive) {
                event.setCancelled(true);
            }
        }
    }
}
