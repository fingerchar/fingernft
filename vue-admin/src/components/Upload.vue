<template>
  <div class="upload">
    <div v-if="limitNum == 1">
      <div v-if="imageUrl && !uploadStatus" class="img-box">
        <img :src="imageUrl" />
      </div>
      <el-upload
        :ref="refName"
        action=""
        v-if="uploadStatus"
        :show-file-list="false"
        list-type="card"
        :auto-upload="false"
        :limit="limitNum"
        class="avatar-uploader picture-card"
        :on-change="
          (file) => {
            uploadChange(file);
          }
        "
        :on-exceed="
          (file) => {
            uploadChange(file);
          }
        "
      >
        <div v-if="imageUrl" class="img-box"><img :src="imageUrl" /></div>
        <el-icon class="icon-plus" v-else><Plus /></el-icon>
      </el-upload>
    </div>
    <div v-else class="upload-box">
      <div
        v-for="item in imageArr"
        :key="item"
        class="img-box"
        :class="uploadStatus ? 'border-box' : ''"
      >
        <img :src="item" />
        <div class="img-remove" v-if="uploadStatus">
          <el-icon @click="removeImageArr(item)"><Delete /></el-icon>
        </div>
      </div>
      <div
        v-for="(item, index) in images"
        :key="index"
        class="img-box"
        :class="uploadStatus ? 'border-box' : ''"
      >
        <img :src="item.url" />
        <div class="img-remove" v-if="uploadStatus">
          <el-icon @click="removeImageArr(item)"><Delete /></el-icon>
        </div>
      </div>
      <el-upload
        :class="canUpload ? '' : 'dis-no'"
        :ref="refName"
        action=""
        v-if="uploadStatus"
        :show-file-list="false"
        :auto-upload="false"
        :limit="limitNum"
        class="avatar-uploader"
        :on-change="
          (file) => {
            uploadChange(file);
          }
        "
      >
        <el-icon class="icon-plus"><Plus /></el-icon>
      </el-upload>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Upload",
    data() {
      return {
        images: [],
        imageArr: [],
        imageUrl: "",
        imageFile: [],
        refName: null,
      };
    },
    props: {
      limitNum: {
        type: Number,
        default: 1,
      },
      imageData: {
        type: [String, Object, File],
        default: "",
      },
      uploadStatus: {
        type: Boolean,
        default: false,
      },
    },
    computed: {
      canUpload() {
        let num = this.imageArr.length + this.images.length;
        if (num >= this.limitNum) {
          return false;
        }
        return true;
      },
    },
    watch: {
      imageData() {
        this.initData();
      },
    },
    mounted() {
      this.initData();
    },
    methods: {
      initData() {
        if (this.limitNum == 1 && this.imageData) {
          this.imageUrl = this.imageData;
        } else if (this.imageData) {
          this.imageArr = this.imageData.split(",");
          this.images = [];
        }
      },
      uploadChange(file) {
        var event = event || window.event;
        var files = event.target.files[0];
        var reader = new FileReader();
        var _this = this;
        if (this.limitNum == 1) {
          this.imageFile = file.raw;
          reader.onload = function (e) {
            _this.imageUrl = e.target.result;
          };
        } else {
          reader.onload = function (e) {
            _this.images.push({ url: e.target.result, file: file.raw });
          };
        }
        reader.readAsDataURL(files);
        this.updateData();
      },
      removeImages(file) {
        let list = this.images;
        for (let i in list) {
          if (list[i].file.uid == file.file.uid) {
            list.splice(i, 1);
          }
        }
        this.images = list;
        let uploadFiles = this.$refs[this.refName].uploadFiles;
        for (let i in uploadFiles) {
          if (uploadFiles[i].uid == file.file.uid) {
            uploadFiles.splice(i, 1);
          }
        }
      },
      removeImageArr(file) {
        let list = this.imageArr;
        for (let i in list) {
          if (list[i] == file) {
            list.splice(i, 1);
          }
        }
        this.imageArr = list;
      },
      cancel() {
        if (this.limitNum == 1) {
          this.imageUrl = "";
        } else {
          this.imageArr = [];
          this.images = [];
        }
      },
      async updateData() {
        if (this.limitNum == 1) {
          this.$emit("updateData", this.imageFile);
        } else {
          let list = this.images;
          let result = [];
          for (let i in list) {
            result.push(list[i].file);
          }
          this.$emit("updateData", result);
        }
      },
    },
  };
</script>

<style lang="scss" scoped>
  .upload {
    padding: 10px 0;
    margin: 10px 0;
    .img-box {
      width: 150px;
      height: 150px;
      max-width: 100%;
      max-height: 100%;
      overflow: hidden;
      display: inline-flex;
      justify-content: center;
      align-items: center;
      position: relative;
      margin: 0 10px 10px 0;
      float: left;
      img {
        width: auto;
        height: auto;
        max-width: 100%;
        max-height: 100%;
      }
    }
    .upload-box {
      .img-remove {
        width: 100%;
        height: 100%;
        cursor: pointer;
        font-size: 24px;
        position: absolute;
        top: 0;
        left: 0;
        opacity: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #fff;
      }
      .img-remove:hover {
        background: #333;
        opacity: 0.8;
      }
    }
    .btn-box {
      margin: 20px 0;
      padding-left: 20px;
    }
  }
  .dis-no {
    display: none;
  }
  .border-box {
    border: 1px dashed #aaa;
    border-radius: 5px;
  }
  :deep(.el-upload--card) {
    height: 148px;
    width: 148px;
    border-radius: 6px;
    border: 1px dashed #cdd0d6;
    .el-icon {
      font-size: 60px;
    }
  }
</style>
