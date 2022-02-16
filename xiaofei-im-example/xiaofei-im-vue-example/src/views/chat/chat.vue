<template>
  <div class="content">
    <div class="msg-list">
      <div class="bind">
        <div class="user-id">
          <el-input v-model="senderId" placeholder="请输入用户ID"/>
        </div>
        <el-button  type="primary" @click="onBind">连接</el-button>
      </div>
      <chat-list @currentUserId="this.currentUserId" />
    </div>
    <div class="chat-box">
      <message/>
      <xiaofei-text :sender-id="senderId" :receiver-id="receiverId"/>
    </div>
  </div>
</template>

<script>
import chatList from '@/components/chatlist/chatlist'
import message from '@/components/message/message'
import XiaofeiText from '@/components/text/xiaofei-text'
export default {
  name: 'chat',
  components: {
    chatList,
    message,
    XiaofeiText,
  },
  data() {
    return {
      senderId: '',
      receiverId: ''
    }
  },
  created() {
    this.$store.dispatch('initData')

  },
  methods: {
    onBind(){
      this.$xfIm.connect(this.receiveHandler,this.senderId,null);
    },
    receiveHandler(msg) {
      console.log(msg)
    },
    currentUserId(id){
      this.receiverId = id;
    }
  }
}
</script>

<style lang="stylus" scoped>

.bind
  display flex
  margin-top 10px
  justify-content space-evenly
.user-id
  width 170px






.content
  display: flex
  width: 800px
  height: 600px
  margin: auto
  border 1px solid #4cae4c

  .msg-list
    width: 250px
    background: white
    border-right: 1px solid #e7e7e7

  .chat-box
    flex: 1
</style>

