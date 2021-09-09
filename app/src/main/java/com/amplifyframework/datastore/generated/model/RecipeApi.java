package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the RecipeApi type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RecipeApis", authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class RecipeApi implements Model {
  public static final QueryField ID = field("RecipeApi", "id");
  public static final QueryField RECIPE_ID = field("RecipeApi", "recipeID");
  public static final QueryField NAME = field("RecipeApi", "name");
  public static final QueryField PHOTO = field("RecipeApi", "photo");
  public static final QueryField OWNER = field("RecipeApi", "owner");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String recipeID;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String", isRequired = true) String photo;
  private final @ModelField(targetType="String", isRequired = true) String owner;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getRecipeId() {
      return recipeID;
  }
  
  public String getName() {
      return name;
  }
  
  public String getPhoto() {
      return photo;
  }
  
  public String getOwner() {
      return owner;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RecipeApi(String id, String recipeID, String name, String photo, String owner) {
    this.id = id;
    this.recipeID = recipeID;
    this.name = name;
    this.photo = photo;
    this.owner = owner;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RecipeApi recipeApi = (RecipeApi) obj;
      return ObjectsCompat.equals(getId(), recipeApi.getId()) &&
              ObjectsCompat.equals(getRecipeId(), recipeApi.getRecipeId()) &&
              ObjectsCompat.equals(getName(), recipeApi.getName()) &&
              ObjectsCompat.equals(getPhoto(), recipeApi.getPhoto()) &&
              ObjectsCompat.equals(getOwner(), recipeApi.getOwner()) &&
              ObjectsCompat.equals(getCreatedAt(), recipeApi.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), recipeApi.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRecipeId())
      .append(getName())
      .append(getPhoto())
      .append(getOwner())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RecipeApi {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("recipeID=" + String.valueOf(getRecipeId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("photo=" + String.valueOf(getPhoto()) + ", ")
      .append("owner=" + String.valueOf(getOwner()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RecipeIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static RecipeApi justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new RecipeApi(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      recipeID,
      name,
      photo,
      owner);
  }
  public interface RecipeIdStep {
    NameStep recipeId(String recipeId);
  }
  

  public interface NameStep {
    PhotoStep name(String name);
  }
  

  public interface PhotoStep {
    OwnerStep photo(String photo);
  }
  

  public interface OwnerStep {
    BuildStep owner(String owner);
  }
  

  public interface BuildStep {
    RecipeApi build();
    BuildStep id(String id) throws IllegalArgumentException;
  }
  

  public static class Builder implements RecipeIdStep, NameStep, PhotoStep, OwnerStep, BuildStep {
    private String id;
    private String recipeID;
    private String name;
    private String photo;
    private String owner;
    @Override
     public RecipeApi build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RecipeApi(
          id,
          recipeID,
          name,
          photo,
          owner);
    }
    
    @Override
     public NameStep recipeId(String recipeId) {
        Objects.requireNonNull(recipeId);
        this.recipeID = recipeId;
        return this;
    }
    
    @Override
     public PhotoStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public OwnerStep photo(String photo) {
        Objects.requireNonNull(photo);
        this.photo = photo;
        return this;
    }
    
    @Override
     public BuildStep owner(String owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String recipeId, String name, String photo, String owner) {
      super.id(id);
      super.recipeId(recipeId)
        .name(name)
        .photo(photo)
        .owner(owner);
    }
    
    @Override
     public CopyOfBuilder recipeId(String recipeId) {
      return (CopyOfBuilder) super.recipeId(recipeId);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder photo(String photo) {
      return (CopyOfBuilder) super.photo(photo);
    }
    
    @Override
     public CopyOfBuilder owner(String owner) {
      return (CopyOfBuilder) super.owner(owner);
    }
  }
  
}
