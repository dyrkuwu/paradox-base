package gay.paradox.mixin;

import com.mojang.authlib.GameProfile;
import gay.paradox.Paradox;
import gay.paradox.event.EventPhase;
import gay.paradox.event.impl.EventMove;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;
    @Shadow
    private double lastX;
    @Shadow
    private double lastBaseY;
    @Shadow
    private double lastZ;
    @Shadow
    private float lastYaw;
    @Shadow
    private float lastPitch;
    @Shadow
    private boolean lastOnGround;
    @Shadow
    private boolean lastSneaking;
    @Shadow
    private int ticksSinceLastPositionPacketSent;
    @Shadow
    @Final
    protected MinecraftClient client;
    @Shadow
    private boolean autoJumpEnabled = true;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    protected abstract boolean isCamera();
    @Shadow
    protected abstract void sendSprintingPacket();

    /**
     * @author ley
     * @reason gay
     */
    @Overwrite
    private void sendMovementPackets() {
        EventMove event = new EventMove(EventPhase.PRE, this.getX(), this.getY(), this.getZ(), this.isOnGround(), this.getYaw(), this.getPitch());
        Paradox.EVENT_BUS.post(event);

        if(event.isCancelled()) return;

        this.sendSprintingPacket();
        boolean bl = this.isSneaking();
        if (bl != this.lastSneaking) {
            ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
            this.lastSneaking = bl;
        }

        if (this.isCamera()) {
            double d = event.getX() - this.lastX;
            double e = event.getY() - this.lastBaseY;
            double f = event.getZ() - this.lastZ;
            double g = event.getYaw() - this.lastYaw;
            double h = event.getPitch() - this.lastPitch;
            ++this.ticksSinceLastPositionPacketSent;
            boolean bl2 = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl3 = g != 0.0 || h != 0.0;
            if (this.hasVehicle()) {
                Vec3d vec3d = this.getVelocity();
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, (double)-999.0F, vec3d.z, (float)event.getYaw(), (float)event.getPitch(), event.isOnGround()));
                bl2 = false;
            } else if (bl2 && bl3) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(event.getX(), event.getY(), event.getZ(), (float)event.getYaw(), (float)event.getPitch(), event.isOnGround()));
            } else if (bl2) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(event.getX(), event.getY(), event.getZ(), event.isOnGround()));
            } else if (bl3) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float)event.getYaw(), (float)event.getPitch(), event.isOnGround()));
            } else if (this.lastOnGround != event.isOnGround()) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(event.isOnGround()));
            }

            if (bl2) {
                this.lastX = event.getX();
                this.lastBaseY = event.getY();
                this.lastZ = event.getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }

            if (bl3) {
                this.lastYaw = (float)event.getYaw();
                this.lastPitch = (float)event.getPitch();
            }

            this.lastOnGround = event.isOnGround();
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
        }

        EventMove event2 = new EventMove(EventPhase.POST, this.getX(), this.getY(), this.getZ(), this.isOnGround(), this.getYaw(), this.getPitch());
        Paradox.EVENT_BUS.post(event2);
    }
}
