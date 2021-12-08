
export default {
  checkResponse(response){
    if(!response.errno){
      return true;
    }
    return false;
  }
}
