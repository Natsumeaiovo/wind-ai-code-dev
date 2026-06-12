<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams">
      <a-form-item label="id">
        <a-input v-model:value="searchParams.id" placeholder="输入id" />
      </a-form-item>
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSearch">搜索</a-button>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" danger ghost @click="doReset">重置</a-button>
      </a-form-item>
    </a-form>
    <a-divider />

    <!-- 表格 -->
    <a-table :columns="columns" :data-source="data" :pagination="pagination" @change="doTableChange" bordered>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="50" :height="50" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button type="primary" danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { deleteUser, listUserVoByPage } from '@/api/userController';
import { Modal, message, type TablePaginationConfig } from 'ant-design-vue';
import dayjs from 'dayjs';
import { computed, onMounted, reactive, ref } from 'vue';
const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'description',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    key: 'action',
  },
];

// 展示的数据
const data = ref<API.UserVO[]>([])
// 总条数
const total = ref(0)

//  默认分页查询参数
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取后端数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    // 更新数据和总条数
    data.value = res.data.data.records || [];
    total.value = res.data.data.totalRow || 0;
  } else {
    message.error('获取用户列表失败，' + res.data.message);
  }
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

const doTableChange = (page: TablePaginationConfig) => {
  searchParams.pageNum = page.current;
  searchParams.pageSize = page.pageSize;
  fetchData();
}

const doSearch = () => {
  searchParams.pageNum = 1;
  fetchData();
}

const doReset = () => {
  searchParams.pageNum = 1;
  searchParams.id = undefined;
  searchParams.userAccount = '';
  searchParams.userName = '';
  fetchData();
}

// 删除用户
const doDelete = async (id: number) => {
  if (!id) {
    return;
  }
  // 确认删除
  Modal.confirm({
    title: '删除确认',
    content: '确定删除用户？',
    centered: true,
    okText: '确定',
    cancelText: '取消',
    okType: 'danger',
    async onOk() {
      const res = await deleteUser({ id });
      if (res.data.data) {
        message.success('删除成功');
        fetchData();
      } else {
        message.error('删除失败，' + res.data.message);
      }
    },
  });
}



// 页面加载时获取数据
onMounted(() => {
  fetchData();
})

</script>
