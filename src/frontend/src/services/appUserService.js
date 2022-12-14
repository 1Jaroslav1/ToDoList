import axios from "axios";
import authHeader from "./authHeader";
const API_URL = "http://localhost:8080/user";


const getAllUsers = async () => {
    return await axios.get(API_URL + "/all", {headers: authHeader() }).then(res => res.data).catch(error => console.log(error));
}

const getUser = () => {
    const userId = JSON.parse(localStorage.getItem("accessToken")).id;

    return axios.get(API_URL + "/get", {headers: authHeader(), params: {id: userId}}).catch(error => console.log(error));
};

const getTeams = async () => {
    const userId = JSON.parse(localStorage.getItem("accessToken")).id;

    return await axios.get(API_URL + "/teams", {headers: authHeader(), params: {id: userId}}).then(res => res.data).catch(error => console.log(error));
}

const getTasks = async (userId=JSON.parse(localStorage.getItem("accessToken")).id) => {
    return await axios.get(API_URL + "/tasks", {headers: authHeader(), params: {id: userId}}).then(res => res.data).catch(error => console.log(error));
}


const getImage = async (userId) => {
    return await axios.get(API_URL + "/image", {headers: authHeader(), params: {id: userId}, responseType: "multipart"}).then(res => res).catch(error => console.log(error));
}

const editUser = async (firstName, lastName) => {
    let formData = new FormData();
    formData.append("firstName", firstName);
    formData.append("lastName", lastName);

    const userId = JSON.parse(localStorage.getItem("accessToken")).id;
    return axios({
        method: 'put',
        url: API_URL + "/edit",
        params: {
            id: userId,
        },
        data : formData,
        headers: authHeader()
    }).then(res => res.data).catch((error) => console.log(error));
}

const editWithImageUser = async (firstName, lastName, image) => {
    let formData = new FormData();
    formData.append("firstName", firstName);
    formData.append("lastName", lastName);
    formData.append("image", image);

    const userId = JSON.parse(localStorage.getItem("accessToken")).id;
    return axios({
        method: 'put',
        url: API_URL + "/edit_with_image",
        params: {
            id: userId,
        },
        data : formData,
        headers: authHeader()
    }).then(res => res.data);
}

const changeUserRole = async (userId, role) => {
    axios({
        method: 'put',
        url: API_URL + "/change_role",
        params: {
            id: userId,
            role: role,
        },
        headers: authHeader()
    }).then(res => res.data).catch((error) => console.log(error));
}

const AppUserService = {
    getAllUsers,
    getUser,
    getTeams,
    getTasks,
    getImage,
    editUser,
    editWithImageUser,
    changeUserRole
};

export default AppUserService;