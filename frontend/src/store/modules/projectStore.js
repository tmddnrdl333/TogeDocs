import { getProjects } from "@/api/project.js";

const projectStore = {
  namespaced: true,
  state: {
    projects: [
      {
        title: "바닷마을다이어리",
        members: [
          "황재완",
          "홍인호",
          "강병국",
          "강병국",
          "강병국",
          "강병국",
          "강병국",
        ],
        url: "https://github.com/JaewanHwang",
        desc: "1번 프로젝트1번1번 프로젝트1번 프로젝트1번 프로젝트 프로젝트1번 프로젝트1번 프로젝트1번 프로젝트1번 프로젝트",
      },
      {
        title: "트랜디it",
        members: ["황재완", "홍인호", "강병국"],
        url: "https://github.com/JaewanHwang",
        desc: "2번 프로젝트",
      },
      {
        title: "투게독스",
        members: ["황재완", "홍인호", "강병국"],
        url: "https://github.com/JaewanHwang",
        desc: "2번 프로젝트",
      },
      {
        title: "싸피독스",
        members: ["황재완", "홍인호", "강병국"],
        url: "https://github.com/JaewanHwang",
        desc: "2번 프로젝트",
      },
      {
        title: "2",
        members: ["황재완", "홍인호", "강병국"],
        url: "https://github.com/JaewanHwang",
        desc: "2번 프로젝트",
      },
      {
        title: "2",
        members: ["황재완", "홍인호", "강병국"],
        url: "https://github.com/JaewanHwang",
        desc: "2번 프로젝트",
      },
    ],
  },
  getters: {},
  mutations: {
    SET_PROJECTS: (state, projects) => {
      state.projects = projects;
    },
  },
  actions: {
    FETCH_PROJECTS: ({ commit }, params) => {
      return getProjects(params, ({ data }) => {
        commit("SET_PROJECTS", data);
      });
    },
  },
};

export default projectStore;
