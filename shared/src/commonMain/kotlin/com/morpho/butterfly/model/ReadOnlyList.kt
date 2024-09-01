package com.morpho.butterfly.model

import com.morpho.butterfly.ImmutableListSerializer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable


typealias ReadOnlyList<T> = @Serializable(with = ImmutableListSerializer::class) ImmutableList<T>

