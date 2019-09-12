package fr.urssaf.image.sae.metadata.control.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.rules.MetadataExistingRule;
import fr.urssaf.image.sae.metadata.rules.MetadataIsArchivableRule;
import fr.urssaf.image.sae.metadata.rules.MetadataIsConsultableRule;
import fr.urssaf.image.sae.metadata.rules.MetadataIsModifiableRule;
import fr.urssaf.image.sae.metadata.rules.MetadataIsSearchableRule;
import fr.urssaf.image.sae.metadata.rules.MetadataIsTransferableRule;
import fr.urssaf.image.sae.metadata.rules.MetadataValueIsRequiredRule;
import fr.urssaf.image.sae.metadata.rules.UntypedMetadataIsRequiredRule;
import fr.urssaf.image.sae.metadata.rules.UntypedMetadataValueLengthRule;
import fr.urssaf.image.sae.metadata.rules.UntypedMetadataValueTypeRule;

/**
 * Classe qui fournit des instances de règles
 */
@Component
@Qualifier("ruleFactory")
@SuppressWarnings("PMD.LongVariable")
public class MetadataRuleFactory {
   @Autowired
   private MetadataValueIsRequiredRule requiredValueRule;

   @Autowired
   private MetadataIsArchivableRule archivableRule;
   @Autowired
   private MetadataIsSearchableRule searchableRule;
   @Autowired
   private MetadataIsConsultableRule consultableRule;
   @Autowired
   private UntypedMetadataValueTypeRule valueTypeRule;
   @Autowired
   private MetadataExistingRule existingRule;
   @Autowired
   private MetadataIsModifiableRule modifiableRule;
   @Autowired
   private MetadataIsTransferableRule transferableRule;

   @Autowired
   private UntypedMetadataValueLengthRule valueLengthRule;

   @Autowired
   private UntypedMetadataIsRequiredRule requiredValueTypeRule;

   /**
    * @return Une instance de la règle {@link MetadataExistingRule}
    */
   public final MetadataExistingRule getExistingRule() {
      return existingRule;
   }

   /**
    * @param existingRule
    *           : Une instance de la règle {@link MetadataExistingRule}
    */
   public final void setExistingRule(final MetadataExistingRule existingRule) {
      this.existingRule = existingRule;
   }

   /**
    * @return Une instance de la règle {@link MetadataIsArchivableRule}
    */
   public final MetadataIsArchivableRule getArchivableRule() {
      return archivableRule;
   }

   /**
    * @param archivableRule
    *           : Une instance de la règle {@link MetadataIsArchivableRule}
    */
   public final void setArchivableRule(
         final MetadataIsArchivableRule archivableRule) {
      this.archivableRule = archivableRule;
   }

   /**
    * @return Une instance de la règle {@link MetadataIsSearchableRule}
    */
   public final MetadataIsSearchableRule getSearchableRule() {
      return searchableRule;
   }

   /**
    * @param searchableRule
    *           : Une instance de la règle {@link MetadataIsSearchableRule}
    * 
    */
   public final void setSearchableRule(
         final MetadataIsSearchableRule searchableRule) {
      this.searchableRule = searchableRule;
   }

   /**
    * @return Une instance de la règle {@link MetadataIsConsultableRule}
    */
   public final MetadataIsConsultableRule getConsultableRule() {
      return consultableRule;
   }

   /**
    * @param consultableRule
    *           : Une instance de la règle {@link MetadataIsConsultableRule}
    * 
    */
   public final void setConsultableRule(
         final MetadataIsConsultableRule consultableRule) {
      this.consultableRule = consultableRule;
   }

   /**
    * @return Une instance de la règle {@link UntypedMetadataValueLengthRule}
    */
   public final UntypedMetadataValueLengthRule getValueLengthRule() {
      return valueLengthRule;
   }

   /**
    * @param valueLengthRule
    *           : Une instance de la règle
    *           {@link UntypedMetadataValueLengthRule}
    * 
    */
   public final void setValueLengthRule(
         final UntypedMetadataValueLengthRule valueLengthRule) {
      this.valueLengthRule = valueLengthRule;
   }

   /**
    * @return Une instance de la règle {@link UntypedMetadataValueTypeRule}
    */
   public final UntypedMetadataValueTypeRule getValueTypeRule() {
      return valueTypeRule;
   }

   /**
    * @param uValueTypeRule
    *           : Une instance de la règle {@link UntypedMetadataValueTypeRule}
    */
   public final void setuValueTypeRule(
         final UntypedMetadataValueTypeRule uValueTypeRule) {
      this.valueTypeRule = uValueTypeRule;
   }

   /**
    * @param valueTypeRule
    *           : Une instance de la règle {@link UntypedMetadataValueTypeRule}.
    */
   public final void setValueTypeRule(
         final UntypedMetadataValueTypeRule valueTypeRule) {
      this.valueTypeRule = valueTypeRule;
   }

   /**
    * @param requiredValueRule
    *           : Une instance de la règle {@link MetadataValueIsRequiredRule}.
    */
   public final void setRequiredValueRule(
         final MetadataValueIsRequiredRule requiredValueRule) {
      this.requiredValueRule = requiredValueRule;
   }

   /**
    * @return Une instance de la règle {@link MetadataValueIsRequiredRule}.
    */
   public final MetadataValueIsRequiredRule getRequiredValueRule() {
      return requiredValueRule;
   }

   /**
    * @return une instance de la règle {@link MetadataIsModifiableRule}
    */
   public final MetadataIsModifiableRule getModifiableRule() {
      return modifiableRule;
   }

   /**
    * @param modifiableRule
    *           une instance de la règle {@link MetadataIsModifiableRule}
    */
   public final void setModifiableRule(MetadataIsModifiableRule modifiableRule) {
      this.modifiableRule = modifiableRule;
   }

   /**
    * Getter pour requiredValueTypeRule
    * 
    * @return the requiredValueTypeRule
    */
   public UntypedMetadataIsRequiredRule getRequiredValueTypeRule() {
      return requiredValueTypeRule;
   }

   /**
    * Setter pour requiredValueTypeRule
    * 
    * @param requiredValueTypeRule
    *           the requiredValueTypeRule to set
    */
   public void setRequiredValueTypeRule(
         UntypedMetadataIsRequiredRule requiredValueTypeRule) {
      this.requiredValueTypeRule = requiredValueTypeRule;
   }

  /**
   * @return the transferableRule
   */
  public MetadataIsTransferableRule getTransferableRule() {
    return transferableRule;
  }

  /**
   * @param transferableRule the transferableRule to set
   */
  public void setTransferableRule(MetadataIsTransferableRule transferableRule) {
    this.transferableRule = transferableRule;
  }

}
