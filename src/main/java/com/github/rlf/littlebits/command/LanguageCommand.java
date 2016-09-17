package com.github.rlf.littlebits.command;

import dk.lockfuglsang.minecraft.command.AbstractCommand;
import dk.lockfuglsang.minecraft.po.I18nUtil;
import dk.lockfuglsang.minecraft.yml.YmlConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;

import static dk.lockfuglsang.minecraft.po.I18nUtil.tr;

/**
 * Supports setting the language.
 */
public class LanguageCommand extends AbstractCommand {

    private JavaPlugin plugin;

    public LanguageCommand(JavaPlugin plugin) {
        super("lang|l", "littlebits.lang", "language", tr("changes the language of the plugin, and reloads"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        if (args.length == 1) {
            Locale loc = I18nUtil.getLocale(args[0]);
            plugin.getConfig().set("language", args[0]);
            I18nUtil.setLocale(Locale.forLanguageTag(args[0]));
            plugin.saveConfig();
            plugin.onDisable();
            plugin.onEnable();
            I18nUtil.clearCache();
            if (I18nUtil.getLocale().equals(I18nUtil.getI18n().getLocale())) {
                sender.sendMessage(tr("\u00a7aSuccessfully changed language to \u00a7e{0}", loc));
            } else {
                sender.sendMessage(tr("\u00a7cFailed to change language to \u00a7e{0}", loc));
            }
            return true;
        } else if (args.length == 0) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("gettext-report.txt");
                 BufferedReader rdr = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = null;
                boolean header = true;
                StringBuilder sb = new StringBuilder();
                sb.append(tr("\u00a79Supported Languages:\n"));
                while ((line = rdr.readLine()) != null) {
                    if (line.startsWith("---")) {
                        header = false;
                    } else if (!header && line.contains("|")) {
                        String parts[] = line.split("\\|");
                        if (parts.length == 7) {
                            sb.append(tr("\u00a7f{0} \u00a77{1} \u00a79 by {2} \u00a77{3}\n", parts[1].trim(), parts[0].trim(), parts[6].trim(), parts[2].trim()));
                        }
                    }
                }
                sender.sendMessage(sb.toString().split("\n"));
            } catch (IOException e) {
                sender.sendMessage(tr("\u00a7cUnable to locate any languages."));
            }
            return true;
        }
        return false;
    }
}
