<!-- 聊天列表 -->
<template>
  <div class="msg-list">
    <ul>
      <li v-for="item in chatList" :class="{ active: item.id === selectId }" class="user-list"
          @click="selectUser(item.id)">
        <div class="list-left">
          <img :alt="item.user.name" :src="item.user.img" class="avatar" height="42" width="42">
        </div>
        <div class="list-right">
          <p class="name">{{ item.user.name }}</p>
          <span class="time">{{ item.messages[item.messages.length - 1].date | time }}</span>
          <p class="recent-msg">{{ item.messages[item.messages.length - 1].content }}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: 'chatList',
  data() {
    return {
      selectId: '',
      chatList: [
        {
          id: "123",
          user: {
            name: '妈咪',
            img: 'static/images/mother.jpg'
          },
          messages: [
            {
              content: '么么哒，妈咪爱你',  //聊天内容
              date: new Date()  //时间
            },
            {
              content: '按回车可以发送信息，还可以给我发送表情哟',
              date: new Date()
            }
          ],
          index: 1  // 当前在聊天列表中的位置,从1开始

        },
        {
          id: "234",
          user: {
            name: 'father',
            img: 'static/images/father.jpg'
          },
          messages: [
            {
              content: 'Are you kidding me?',
              date: new Date()
            }
          ],
          index: 2
        },
        {
          id: "345",
          user: {
            name: '机器人',
            img: 'static/images/vue.jpg'
          },
          messages: [
            {
              content: '我会跟你聊聊天的哟',
              date: new Date()
            }
          ],
          index: 3
        }
      ],
    }
  },
  methods: {
    selectUser(id){
      this .selectId = id;
      this.$emit("currentUserId",id);
    }
  },
  filters: {
    // 将日期过滤为 hour:minutes
    time(date) {
      if (typeof date === 'string') {
        date = new Date(date);
      }
      if (date.getMinutes() < 10) {
        return date.getHours() + ':0' + date.getMinutes();
      } else {
        return date.getHours() + ':' + date.getMinutes();
      }
    }
  },
}
</script>

<style lang="stylus" scoped>
.msg-list
  height: 540px
  overflow-y: auto

  .user-list
    display: flex
    padding: 12px
    transition: background-color .1s
    font-size: 0
    border-bottom 1px solid #5bc0de
    &:hover
      background-color: rgb(220, 220, 220)

    &.active
      background-color: #c4c4c4

    .avatar
      border-radius: 2px
      margin-right: 12px

    .list-right
      position: relative
      flex: 1
      margin-top: 4px

    .name
      display: inline-block
      vertical-align: top
      font-size: 14px

    .time
      float: right
      color: #999
      font-size: 10px
      vertical-align: top

    .recent-msg
      position: absolute
      font-size: 12px
      width: 130px
      height: 15px
      line-height: 15px
      color: #999
      bottom: -15px
      overflow: hidden
      white-space: nowrap
      text-overflow: ellipsis
</style>
