package com.volunteers.anotations;

import com.volunteers.areas.events.models.binding.RegisterEventModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsEndDateBeforeStartDate implements ConstraintValidator<EndDateBeforeStartDate, Object> {
   public void initialize(EndDateBeforeStartDate constraint) {
   }

   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      boolean isValidDate = false;
      if (((RegisterEventModel)obj).getEndDate() != null && (((RegisterEventModel)obj).getStartDate()) != null ) {
         isValidDate = ((RegisterEventModel) obj).getEndDate().after(((RegisterEventModel) obj).getStartDate());
      }
      return isValidDate;
   }
}
