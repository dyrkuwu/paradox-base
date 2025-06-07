package gay.paradox.mixin;

import gay.paradox.Paradox;
import gay.paradox.event.impl.EventPacketRecieve;
import gay.paradox.event.impl.EventPacketSend;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        EventPacketRecieve event = new EventPacketRecieve(packet);
        Paradox.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "send*", at = @At("HEAD"), cancellable = true)
    private void sendPacketEvent(Packet<?> packet, final CallbackInfo ci) {
        EventPacketSend event = new EventPacketSend(packet);
        Paradox.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
