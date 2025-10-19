package omi.serverutils.osutils.commands.region;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;

public class RegionOutputCommandUtility {
    private static final int MAX_LENGTH = 270;

    public static Component getRegionList(ArrayList<RegionsModule.Region> regionList, String dimension){

        String regionsJson = "";
        for(RegionsModule.Region region: regionList){

            String thisRegion = "";
            String thisRegionDimension = "";
            String thisRegionActions = "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"[+]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"green\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Show Info of \\\"" +  region.name + "\\\"\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/region info " + region.dimension + " " + region.name + "\"}},";

            if (dimension.equals("all dimensions")) {
                thisRegionDimension = "{\"text\":\" [" + region.dimension + "]\", \"color\":\"dark_gray\",\"strikethrough\":false},";
            }

            thisRegion += thisRegionActions; // Buttons
            thisRegion += thisRegionDimension; // Adds the [minecraft:overworld] line (if specified)
            thisRegion += "{\"text\": \" " + region.name + " \\n\", \"color\": \"gold\", \"strikethrough\": false},"; // Region name

            regionsJson += thisRegion; // Adds the component to the region list
        }

        String json = "[ {\"text\": \"-------\", \"color\": \"yellow\", \"strikethrough\": true}, {\"text\":\" Regions in " + dimension + " \", \"color\": \"gold\", \"strikethrough\": false}, {\"text\": \"-------\\n\", \"color\": \"yellow\", \"underline\": true}," +
                regionsJson +
                "{\"text\": \"------------------------------------\", \"color\": \"yellow\", \"strikethrough\": true}]";

        return Component.Serializer.fromJson(json);
    }


    public static Component getRegionInfo(RegionsModule.Region region) {
        String stringJson = "";

        OSUtils.LOGGER.debug("Getting region info");

        String modifyNameAction = "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"[/]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"blue\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Rename region\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/region modify " + region.dimension + " " + region.name + " name \"}},";
        String toggleEnabledAction = "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"[/]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"blue\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Toggle enabled state\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/region modify " + region.dimension + " " + region.name + " enabled " + !region.enabled + "\"}},";
        String modifyDimensionAction = "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"[/]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"blue\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Change region dimension\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/region modify " + region.dimension + " " + region.name + " dimension \"}},";
        String addOwnerAction = "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"[+]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"blue\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Add new owner\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/region property set " + region.dimension + " " + region.name + " owners add \"}},";
        String removeAction = "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"[Delete Region]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"red\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Remove region\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/region remove " + region.dimension + " " + region.name + "\"}},";


        stringJson += "[ {\"text\": \"-------\", \"color\": \"yellow\", \"strikethrough\": true}, {\"text\":\" Region Info \", \"color\": \"gold\", \"strikethrough\": false}, {\"text\": \"-------\\n\", \"color\": \"yellow\", \"underline\": true},";

        stringJson += modifyNameAction;
        stringJson += "{ \"text\": \" Name: \", \"color\": \"gold\", \"strikethrough\": false},";
        stringJson += "{ \"text\": \"" + region.name + "\\n\", \"color\": \"gray\", \"strikethrough\": false},";

        stringJson += toggleEnabledAction;
        stringJson += "{ \"text\": \" Enabled: \", \"color\": \"gold\", \"strikethrough\": false},";
        stringJson += "{ \"text\": \"" + region.enabled + "\\n\", \"color\": \"" + (region.enabled ? "green" : "red") + "\", \"strikethrough\": false},";

        stringJson += modifyDimensionAction;
        stringJson += "{ \"text\": \" Dimension: \", \"color\": \"gold\", \"strikethrough\": false},";
        stringJson += "{ \"text\": \"" + region.dimension + "\\n\", \"color\": \"gray\", \"strikethrough\": false},";

        stringJson += addOwnerAction;
        stringJson += "{ \"text\": \" Owners:\", \"color\": \"gold\", \"strikethrough\": false},";


        for(String owner: region.owners){
            stringJson += "{ \"text\":\" \", \"strikethrough\":false},{\"text\": \"" + owner + "\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"gray\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Remove " + owner + " as owner\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/region property set " + region.dimension + " " + region.name + " owners remove " + owner + "\"}},";
        }

        stringJson += "{\"text\":\"\\n \", \"underline\":false, \"strikethrough\":false},{ \"text\": \"[-]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"dark_gray\" },";
        stringJson += "{ \"text\": \" Other Actions:\", \"color\": \"gold\", \"strikethrough\": false},";;
        stringJson += removeAction;

        stringJson += "{\"text\":\"\\n \", \"underline\":false, \"strikethrough\":false},{ \"text\": \"[-]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"dark_gray\" },";
        stringJson += "{ \"text\": \" Properties:\", \"color\": \"gold\", \"strikethrough\": false, \"underline\": false},";

        for(String property: region.properties.keySet()){
            boolean state = region.properties.get(property);

            String togglePropertyAction = "{ \"text\":\"\\n      \", \"strikethrough\":false},{\"text\": \"[/]\", \"underlined\":true, \"strikethrough\":false ,\"color\":\"blue\",   \"hoverEvent\" : {\"action\":\"show_text\",  \"contents\"  :  {\"text\": \"Change property value\", \"color\":\"gray\", \"italic\": true} }         , \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/region property set " + region.dimension + " " + region.name + " " + property + " " + !state + "\"}},";
            stringJson += togglePropertyAction;
            stringJson += "{ \"text\": \" " + property + ": \", \"color\": \"gray\", \"strikethrough\": false},";
            stringJson += "{ \"text\": \"" + state + "\", \"color\": \"" + (state ? "green" : "red") + "\", \"strikethrough\": false},";
        }

        stringJson += "{ \"text\": \"\\n-------------------------\", \"color\": \"yellow\", \"strikethrough\": true}]";

        return Component.Serializer.fromJson(stringJson);
    }
}
