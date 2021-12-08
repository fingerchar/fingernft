<template>
  <div class="il-media">
    <template v-if="mediaType === 'image'">
      <el-image
        style="width: 100px; height: 100px"
        :src="mediaUrl"
        :preview-src-list="srcList"
      >
      </el-image>
    </template>
    <template v-else>
      <audio v-if="mediaType === 'audio'" :src="mediaUrl" controls></audio>
      <div class="video-wrap" v-if="mediaType === 'video'">
        <video
        :src="mediaUrl"
        :width="isPreview ? '80%' : '200px'"
        :controls="isPreview ? 'controls' : ''"
      ></video>
      <div @click="dialogVisible=true" v-if="isPreview" class="video-btn"></div>
      </div>
    </template>
    <el-dialog :visible.sync="dialogVisible" class="media-dialog">
        <video
        :src="mediaUrl"
        width="100%"
        controls
      ></video>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "Media",
  data() {
    return {
      dialogVisible: false,
      typeData: {
        image: [".jpeg", ".jpg", ".png", ".gif"],
        audio: [".mp3", ".wav", ".oga"],
        video: [".webm", ".mp4", ".m4v", ".ogg", ".ogv", ".mov"],
      },
    };
  },
  props: {
    url: String,
    type: String,
    // 可选：是否预览
    isPreview: {
      default: false,
    },
  },
  computed: {
    ...mapState({ config: (state) => state.user.config }),
    mediaUrl() {
      if (this.url.startsWith("ipfs", 0)) {
        return this.url.replace("ipfs://", this.config.ipfsUrl);
      } else {
        return this.url;
      }
    },
    mediaType() {
      if (this.type) {
        if (this.type === "image") {
          return "image";
        } else {
          if (this.type.split("/")[0] === "audio") {
            return "audio";
          }
          if (this.type.split("/")[0] === "video") {
            return "video";
          }
        }
      } else {
        for (let key in this.typeData) {
          for(let item of this.typeData[key]) {
            if(this.url.endsWith(item)) {
              return key
            }
          }
        }
        return
      }
    },
    srcList() {
      return this.isPreview ? [this.mediaUrl] : [];
    },
  },
};
</script>

<style lang="scss" scoped>
table audio {
  width: 80%;
}
.il-media:hover {
  cursor: pointer;
}
.video-wrap{
  position:relative
}
.video-btn{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>

