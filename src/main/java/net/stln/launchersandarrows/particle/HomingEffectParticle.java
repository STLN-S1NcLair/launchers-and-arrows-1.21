package net.stln.launchersandarrows.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class HomingEffectParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    public HomingEffectParticle(ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.velocityX = (Math.random() - 0.5) / 100;
        this.velocityY = (Math.random() - 0.5) / 100;
        this.velocityZ = (Math.random() - 0.5) / 100;
        this.maxAge = 6 + this.random.nextInt(9);
        this.alpha = 0.8F;
        this.scale = 0.15F;
        this.gravityStrength = 0.0F;
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);
    }
    @Override
    public int getBrightness(float tint) {
        int i = this.maxAge / 2;
        return (int) Math.max(15728880 - (this.age >= i ? ((float) (this.age - i) / i * 7864440) : 0), tint);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.velocityY = this.velocityY - 0.04 * (double)this.gravityStrength;
        }

        this.velocityX = this.velocityX * (double)this.velocityMultiplier;
        this.velocityY = this.velocityY * (double)this.velocityMultiplier;
        this.velocityZ = this.velocityZ * (double)this.velocityMultiplier;

        if (this.age >= this.maxAge * 0.8F) {
                this.alpha = (this.maxAge - this.age) / (this.maxAge * 0.2F) * 0.6F + 0.2F;
            }
            this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }


    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new HomingEffectParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
