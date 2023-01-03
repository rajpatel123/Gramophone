package agstack.gramophone.ui.tv.model

data class VideoBookMarkedRequest(
    val youtube_video_id: String,
    val youtube_video_title: String,
    val youtube_video_desc: String,
    val youtube_video_duration: String,
    val youtube_channel_name: String,
    val is_bookmark: Boolean,
)

