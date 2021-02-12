package com.favouritedragon.arcaneessentials.common.capability;

import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;

//Mwahahahahah
public class ArcaneWizardData extends WizardData {

    /* Saved upon death and across world loading.*/
    private SpellModifiers castModifiers;
    private int castDuration;
    private Spell commandSpell;

    private int level;
    private float xp;
    private float totalXp;
    private int spellSlots;
}
