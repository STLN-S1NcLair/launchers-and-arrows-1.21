package net.stln.launchersandarrows.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class HomingEffectParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    public HomingEffectParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet){
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.xd = (this.random.nextDouble() - 0.5) / 100;
        this.yd = (this.random.nextDouble() - 0.5) / 100;
        this.zd = (this.random.nextDouble() - 0.5) / 100;
        this.lifetime = 6 + this.random.nextInt(9);
        this.alpha = 0.8F;
        this.quadSize = 0.15F;
        this.gravity = 0F;
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    protected int getLightColor(float partialTick) {
        int i = this.lifetime / 2;
        return (int) Math.max(15728880 - (this.age >= i ? ((float) (this.age - i) / i * 7864440) : 0), partialTick);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.move(this.xd, this.yd, this.zd);
        if(this.age++ >= this.lifetime){
            this.remove();
        }
        else {
            this.yd -= 0.04 * (double)this.gravity;
        }
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;

        if(this.age >= this.lifetime * 0.8F){
            this.alpha = (this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F;
        }
        this.setSpriteFromAge(this.spriteSet); //これいる？
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HomingEffectParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}