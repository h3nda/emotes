package io.github.kosmx.emotes.api.proxy;

import io.github.kosmx.emotes.common.network.EmotePacket;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.UUID;

/**
 * To hold information about network
 *
 * implement {@link AbstractNetworkInstance} if you want to implement only the necessary functions
 *
 * use this interface if you want to do something completely different
 */
public interface INetworkInstance {
    /**
     * Get the version from the other side. null if default
     * @return maybe null
     */
    HashMap<Byte, Byte> getVersions();

    /**
     * Receive (and save) versions from the other side
     * @param map map
     */
    void setVersions(HashMap<Byte, Byte> map);

    /**
     * Does send the sender's id to the server
     * @return true means send
     */
    default boolean sendPlayerID(){
        return false;
    }

    /**
     * The Proxy controller ask you to send the message,
     * only if {@link #isActive()} is true
     * @param builder packet builder
     * @param target target to send message, if null, everyone in the view distance
     * @throws IOException when message write to bytes has failed
     */
    void sendMessage(EmotePacket.Builder builder, @Nullable UUID target) throws IOException;

    /**
     * Network instance has received a message, it will send it to EmoteX core to execute
     * you can set your receive event to invoke this
     * there are it's other forms in {@link AbstractNetworkInstance}
     * @param byteBuffer received buffer
     * @param player player who plays the emote, Can be NULL but only if {@link #trustReceivedPlayer()} is true or message is not play or stop
     */
    default void receiveMessage(ByteBuffer byteBuffer, UUID player) {
        EmotesProxyManager.receiveMessage(byteBuffer, player, this);
    }

    /**
     * You are asked to send your config.
     */
    void sendConfigCallback();

    /**
     * when receiving a message, it contains a player. If you don't trust in this information, override this and return false
     *
     * @return false if received info is untrusted
     */
    default boolean trustReceivedPlayer(){
        return true;
    }

    /**
     * Is the other side is available
     * your send won't be invoke if you return false
     *
     * @return is this channel working
     */
    boolean isActive();


    /**
     * If {@link ByteBuffer} is wrapped, it is safe to get the array
     * but if is direct manual read is required.
     * @param byteBuffer get the bytes from
     * @return the byte array
     */
    static byte[] safeGetBytesFromBuffer(ByteBuffer byteBuffer){
        try {
            if (byteBuffer.isDirect() || byteBuffer.isReadOnly()) {
                //not so fast, but there is no other way if the buffer is direct
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                return bytes;
            }
            //Most efficient way.
            else return byteBuffer.array();
        }
        //This shouldn't be able to happen
        catch (BufferOverflowException | BufferUnderflowException e){
            e.printStackTrace();
            return null;
        }
    }
}
