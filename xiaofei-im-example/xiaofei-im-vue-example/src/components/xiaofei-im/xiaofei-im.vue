<template>
  <div>
    <div class="msg-list">
      <ul>
        <li v-for="item in $store.state.chatList" :class="{ active: item.id === selectId }" class="user-list"
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
  </div>
</template>

<script>

export default {
  name: "XiaofeiIm",
  data() {
    return {

    }
  },
  methods: {
    selectUser(id){
      this .selectId = id;
      this.$emit("currentUserId",id);
    }
  }
}
</script>

<style scoped>

</style>
