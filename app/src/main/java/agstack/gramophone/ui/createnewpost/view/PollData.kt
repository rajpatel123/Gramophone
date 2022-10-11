package agstack.gramophone.ui.createnewpost.view

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class PollData :Serializable{
  @JsonProperty("quiz_id")
  var quiz_id: String? = null
  @JsonProperty("question_id")
  var _id: String? = null
  @JsonProperty("image")
    var image: String? = null
  @JsonProperty("question")
    var question: String? = null
  @JsonProperty("quiz_title")
  var question_title: String? = null
  @JsonProperty("answered")
    var answer_submitted: Boolean? = null
  @JsonProperty("active")
  var active: Boolean = false
  @JsonProperty("question_type")
    var choice_option_single: String? = null
  @JsonProperty("quiz_type")
    var poll_type:String?=null
  @JsonProperty("options")
     var poll_options:List<Poll>?=null
}