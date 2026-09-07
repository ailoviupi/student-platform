<template>
  <div class="page-container">
    <div class="page-header"><h2>学籍信息</h2></div>
    <el-card>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="学号">202401001</el-descriptions-item>
        <el-descriptions-item label="姓名">张三</el-descriptions-item>
        <el-descriptions-item label="性别">男</el-descriptions-item>
        <el-descriptions-item label="出生日期">2005-03-15</el-descriptions-item>
        <el-descriptions-item label="身份证号">5109**********1234</el-descriptions-item>
        <el-descriptions-item label="民族">汉族</el-descriptions-item>
        <el-descriptions-item label="政治面貌">共青团员</el-descriptions-item>
        <el-descriptions-item label="籍贯">四川省遂宁市</el-descriptions-item>
        <el-descriptions-item label="联系电话">138****5678</el-descriptions-item>
        <el-descriptions-item label="院系">信息工程系</el-descriptions-item>
        <el-descriptions-item label="专业">软件技术</el-descriptions-item>
        <el-descriptions-item label="班级">软件24-1班</el-descriptions-item>
        <el-descriptions-item label="年级">2024级</el-descriptions-item>
        <el-descriptions-item label="入学日期">2024-09-01</el-descriptions-item>
        <el-descriptions-item label="培养层次">专科</el-descriptions-item>
        <el-descriptions-item label="学习形式">全日制</el-descriptions-item>
        <el-descriptions-item label="学籍状态">
          <el-tag type="success">在读</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>
