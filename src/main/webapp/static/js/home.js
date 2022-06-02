$(function(){
  $('.true-choose-image').on('change', function () {
    console.log("a");
    let src = window.URL.createObjectURL(this.files[0]);
    let $img = $(this).parent().prev();
    console.log($img);
    $img.attr('src', src);
    // $img.css('display', 'block');
    // $('.img-border').css('border', '0');
  })
})