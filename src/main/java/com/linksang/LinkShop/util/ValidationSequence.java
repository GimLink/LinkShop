package com.linksang.LinkShop.util;

import jakarta.validation.GroupSequence;

@GroupSequence({ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class, ValidationGroups.LengthGroup.class, ValidationGroups.PositiveOrZero.class})
public interface ValidationSequence {
}
