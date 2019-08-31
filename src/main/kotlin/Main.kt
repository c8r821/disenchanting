package c8r821.minecraft

import hazae41.minecraft.chestui.gui
import hazae41.minecraft.kotlin.bukkit.BukkitPlugin
import hazae41.minecraft.kotlin.bukkit.listen
import kotlin.math.ceil
import org.bukkit.Material.*
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.Damageable

class Disenchanting : BukkitPlugin() {

    override fun onEnable() {
        listen<PlayerInteractEvent> {
            if (
                it.action == Action.RIGHT_CLICK_BLOCK &&
                it.player.isSneaking &&
                it.clickedBlock?.type == ENCHANTING_TABLE &&
                it.item?.enchantments?.isEmpty() == false &&
                (it.item?.itemMeta as Damageable).damage <= 20
            ) {
                val item = it.item!!
                val itemName = if (item.hasItemMeta() && item.itemMeta!!.hasDisplayName()) item.itemMeta!!.displayName else item.type.toString().split("_").joinToString(" ") { it.toLowerCase().capitalize() }
                var count = 0
                val menu = gui(it.player, "Disenchanting $itemName", ceil(item.enchantments.size / 9.0).toInt()) {
                    all {
                        onclick = { isCancelled = true }
                    }

                    item.enchantments.forEach {
                        item(++count % 9, ceil(count / 9.0).toInt()) {
                            type = LEGACY_ENCHANTED_BOOK
                            onclick = { _ -> 
                                isCancelled = false
                                item.removeEnchantment(it.key)
                                onclick = { isCancelled = true }
                            }
                            storedEnchants[it.key] = it.value
                        }
                    }
                }

                menu.open(it.player)
            }
        }
    }
}

