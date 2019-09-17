function openModal(recipe_id) {
    $.ajax({
        url: "/recipe/"+recipe_id,
        success: function (data) {
            $("#openModal").modal("show");
        }
    })
}

function modalBuild(recipe_id, shortlink) {
    url = document.location['origin'];
    if (shortlink === null){
        shortlink = url + "/recipe/" + recipe_id;
    } else {
        shortlink = url + "/recipe/private/" + shortlink;
    }
    document.getElementById('current_recipe_id').value = recipe_id;
    document.getElementById('shortlinkInput').value = shortlink;

}


$("#share_by_username").click(function (e) {
    $.ajax({
        type: "POST",
        url: "/share_recipe_by_username",
        data: "name="+document.getElementById('nameInput').value+"&recipe_id="+document.getElementById('current_recipe_id').value,
        success: function(result) {
            alert(result.status);
        }
    });

});
