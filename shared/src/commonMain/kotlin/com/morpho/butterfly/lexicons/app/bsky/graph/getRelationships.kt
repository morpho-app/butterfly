package app.bsky.graph

import kotlin.Any
import kotlin.Pair
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtIdentifier
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface GetRelationshipsResponseRelationshipUnion {
  public class RelationshipSerializer : KSerializer<Relationship> by valueClassSerializer(
    serialName = "app.bsky.graph.defs#relationship",
    constructor = ::Relationship,
    valueProvider = Relationship::value,
    valueSerializerProvider = { app.bsky.graph.Relationship.serializer() },
  )

  @Serializable(with = RelationshipSerializer::class)
  @JvmInline
  @SerialName("app.bsky.graph.defs#relationship")
  public value class Relationship(
    public val `value`: app.bsky.graph.Relationship,
  ) : GetRelationshipsResponseRelationshipUnion

  public class NotFoundActorSerializer : KSerializer<NotFoundActor> by valueClassSerializer(
    serialName = "app.bsky.graph.defs#notFoundActor",
    constructor = ::NotFoundActor,
    valueProvider = NotFoundActor::value,
    valueSerializerProvider = { app.bsky.graph.NotFoundActor.serializer() },
  )

  @Serializable(with = NotFoundActorSerializer::class)
  @JvmInline
  @SerialName("app.bsky.graph.defs#notFoundActor")
  public value class NotFoundActor(
    public val `value`: app.bsky.graph.NotFoundActor,
  ) : GetRelationshipsResponseRelationshipUnion
}

@Serializable
public data class GetRelationshipsQuery(
  /**
   * Primary account requesting relationships for.
   */
  public val actor: AtIdentifier,
  /**
   * List of 'other' accounts to be related back to the primary.
   */
  public val others: ReadOnlyList<AtIdentifier> = persistentListOf(),
) {
  init {
    require(others.count() <= 30) {
      "others.count() must be <= 30, but was ${others.count()}"
    }
  }

  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("actor" to actor)
    others.forEach {
      add("others" to it)
    }
  }.toImmutableList()
}

@Serializable
public data class GetRelationshipsResponse(
  public val actor: Did? = null,
  public val relationships: ReadOnlyList<GetRelationshipsResponseRelationshipUnion>,
)
