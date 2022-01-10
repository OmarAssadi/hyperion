package org.hyperion.rs2.net;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Palette;
import org.hyperion.rs2.model.Palette.PaletteTile;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.impl.EquipmentContainerListener;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.container.impl.WeaponContainerListener;
import org.hyperion.rs2.net.Packet.Type;

/**
 * A utility class for sending packets.
 *
 * @author Graham Edgecombe
 */
public class ActionSender {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates an action sender for the specified player.
     *
     * @param player The player to create the action sender for.
     */
    public ActionSender(final Player player) {
        this.player = player;
    }

    /**
     * Sends an inventory interface.
     *
     * @param interfaceId          The interface id.
     * @param inventoryInterfaceId The inventory interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceInventory(final int interfaceId, final int inventoryInterfaceId) {
        player.getInterfaceState().interfaceOpened(interfaceId);
        player.write(new PacketBuilder(248).putShortA(interfaceId).putShort(inventoryInterfaceId).toPacket());
        return this;
    }

    /**
     * Sends all the login packets.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendLogin() {
        player.setActive(true);
        sendDetails();
        sendMessage("Welcome to RuneScape.");

        sendMapRegion();
        sendSidebarInterfaces();

        final InterfaceContainerListener inventoryListener = new InterfaceContainerListener(player, Inventory.INTERFACE);
        player.getInventory().addListener(inventoryListener);

        final InterfaceContainerListener equipmentListener = new InterfaceContainerListener(player, Equipment.INTERFACE);
        player.getEquipment().addListener(equipmentListener);
        player.getEquipment().addListener(new EquipmentContainerListener(player));
        player.getEquipment().addListener(new WeaponContainerListener(player));

        return this;
    }

    /**
     * Sends the initial login packet (e.g. members, player id).
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendDetails() {
        player.write(new PacketBuilder(249).putByteA(player.isMembers() ? 1 : 0).putLEShortA(player.getIndex()).toPacket());
        player.write(new PacketBuilder(107).toPacket());
        return this;
    }

    /**
     * Sends a message.
     *
     * @param message The message to send.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendMessage(final String message) {
        player.write(new PacketBuilder(253, Type.VARIABLE).putRS2String(message).toPacket());
        return this;
    }

    /**
     * Sends the map region load command.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendMapRegion() {
        player.setLastKnownRegion(player.getLocation());
        player.write(new PacketBuilder(73).putShortA(player.getLocation().getRegionX() + 6).putShort(player.getLocation().getRegionY() + 6).toPacket());
        return this;
    }

    /**
     * Sends all the sidebar interfaces.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSidebarInterfaces() {
        final int[] icons = Constants.SIDEBAR_INTERFACES[0];
        final int[] interfaces = Constants.SIDEBAR_INTERFACES[1];
        for (int i = 0; i < icons.length; i++) {
            sendSidebarInterface(icons[i], interfaces[i]);
        }
        return this;
    }

    /**
     * Sends a specific sidebar interface.
     *
     * @param icon        The sidebar icon.
     * @param interfaceId The interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSidebarInterface(final int icon, final int interfaceId) {
        player.write(new PacketBuilder(71).putShort(interfaceId).putByteA(icon).toPacket());
        return this;
    }

    /**
     * Sends the packet to construct a map region.
     *
     * @param palette The palette of map regions.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendConstructMapRegion(final Palette palette) {
        player.setLastKnownRegion(player.getLocation());
        final PacketBuilder bldr = new PacketBuilder(241, Type.VARIABLE_SHORT);
        bldr.putShortA(player.getLocation().getRegionY() + 6);
        bldr.startBitAccess();
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    final PaletteTile tile = palette.getTile(x, y, z);
                    bldr.putBits(1, tile != null ? 1 : 0);
                    if (tile != null) {
                        bldr.putBits(26, tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1);
                    }
                }
            }
        }
        bldr.finishBitAccess();
        bldr.putShort(player.getLocation().getRegionX() + 6);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends the player's skills.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSkills() {
        for (int i = 0; i < Skills.SKILL_COUNT; i++) {
            sendSkill(i);
        }
        return this;
    }

    /**
     * Sends a specific skill.
     *
     * @param skill The skill to send.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSkill(final int skill) {
        final PacketBuilder bldr = new PacketBuilder(134);
        bldr.put((byte) skill);
        bldr.putInt1((int) player.getSkills().getExperience(skill));
        bldr.put((byte) player.getSkills().getLevel(skill));
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends the logout packet.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendLogout() {
        player.write(new PacketBuilder(109).toPacket()); // TODO IoFuture
        return this;
    }

    /**
     * Sends a packet to update a group of items.
     *
     * @param interfaceId The interface id.
     * @param items       The items.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItems(final int interfaceId, final Item[] items) {
        final PacketBuilder bldr = new PacketBuilder(53, Type.VARIABLE_SHORT);
        bldr.putShort(interfaceId);
        bldr.putShort(items.length);
        for (final Item item : items) {
            if (item == null) {
                bldr.put((byte) 0);
                bldr.putLEShortA(0);
            } else {
                final int count = item.getCount();
                if (count > 254) {
                    bldr.put((byte) 255);
                    bldr.putInt2(count);
                } else {
                    bldr.put((byte) count);
                }
                bldr.putLEShortA(item.getId() + 1);
            }
        }
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @param item        The item.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItem(final int interfaceId, final int slot, final Item item) {
        final PacketBuilder bldr = new PacketBuilder(34, Type.VARIABLE_SHORT);
        bldr.putShort(interfaceId).putSmart(slot);
        if (item == null) {
            bldr.putShort(0);
            bldr.put((byte) 0);
        } else {
            bldr.putShort(item.getId() + 1);
            final int count = item.getCount();
            if (count > 254) {
                bldr.put((byte) 255);
                bldr.putInt(count);
            } else {
                bldr.put((byte) count);
            }
        }
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a packet to update multiple (but not all) items.
     *
     * @param interfaceId The interface id.
     * @param slots       The slots.
     * @param items       The item array.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItems(final int interfaceId, final int[] slots, final Item[] items) {
        final PacketBuilder bldr = new PacketBuilder(34, Type.VARIABLE_SHORT).putShort(interfaceId);
        for (int i = 0; i < slots.length; i++) {
            final Item item = items[slots[i]];
            bldr.putSmart(slots[i]);
            if (item == null) {
                bldr.putShort(0);
                bldr.put((byte) 0);
            } else {
                bldr.putShort(item.getId() + 1);
                final int count = item.getCount();
                if (count > 254) {
                    bldr.put((byte) 255);
                    bldr.putInt(count);
                } else {
                    bldr.put((byte) count);
                }
            }
        }
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends the enter amount interface.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendEnterAmountInterface() {
        player.write(new PacketBuilder(27).toPacket());
        return this;
    }

    /**
     * Sends the player an option.
     *
     * @param slot The slot to place the option in the menu.
     * @param top  Flag which indicates the item should be placed at the top.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInteractionOption(final String option, final int slot, final boolean top) {
        final PacketBuilder bldr = new PacketBuilder(104, Type.VARIABLE);
        bldr.put((byte) -slot);
        bldr.putByteA(top ? (byte) 0 : (byte) 1);
        bldr.putRS2String(option);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a string.
     *
     * @param id     The interface id.
     * @param string The string.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendString(final int id, final String string) {
        final PacketBuilder bldr = new PacketBuilder(126, Type.VARIABLE_SHORT);
        bldr.putRS2String(string);
        bldr.putShortA(id);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a model in an interface.
     *
     * @param id    The interface id.
     * @param zoom  The zoom.
     * @param model The model id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceModel(final int id, final int zoom, final int model) {
        final PacketBuilder bldr = new PacketBuilder(246);
        bldr.putLEShort(id).putShort(zoom).putShort(model);
        player.write(bldr.toPacket());
        return this;
    }
}
