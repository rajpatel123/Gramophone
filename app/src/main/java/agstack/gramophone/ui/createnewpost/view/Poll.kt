package agstack.gramophone.ui.createnewpost.view

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Poll :Serializable{
  @JsonProperty("option_id")
  var option_id: String? = null
  @JsonProperty("answer")
    var option_text: String? = null
  @JsonProperty("total_votes")
    var option_result: Int? = null
  @JsonProperty("valid_answer")
    var right_answer: Boolean? = null
  @JsonProperty("option_selected")
  var user_selected: Boolean? = null
}