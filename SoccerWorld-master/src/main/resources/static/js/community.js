document.addEventListener('DOMContentLoaded', function () {
    const csrfToken  = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // 수정 버튼 클릭 시: span 숨기고 textarea, 저장/취소 버튼 노출
    document.querySelectorAll('.edit-comment-btn').forEach(function(button) {
        button.addEventListener('click', function() {
            const commentItem = button.closest('.comment-item');
            const textSpan    = commentItem.querySelector('.comment-text');
            const editTextarea= commentItem.querySelector('.comment-edit-input');
            const saveBtn     = commentItem.querySelector('.save-comment-btn');
            const cancelBtn   = commentItem.querySelector('.cancel-edit-btn');

            // 기존 댓글 내용을 textarea에 설정
            editTextarea.value = textSpan.textContent;
            // 토글
            textSpan.style.display       = 'none';
            editTextarea.style.display   = 'block';
            saveBtn.style.display        = 'inline-block';
            cancelBtn.style.display      = 'inline-block';
            button.style.display         = 'none';
        });
    });

    // 취소 버튼 클릭 시: 원래 상태로 복원
    document.querySelectorAll('.cancel-edit-btn').forEach(function(button) {
        button.addEventListener('click', function() {
            const commentItem = button.closest('.comment-item');
            const textSpan    = commentItem.querySelector('.comment-text');
            const editTextarea= commentItem.querySelector('.comment-edit-input');
            const saveBtn     = commentItem.querySelector('.save-comment-btn');
            const editBtn     = commentItem.querySelector('.edit-comment-btn');

            // 토글 복원
            editTextarea.style.display   = 'none';
            textSpan.style.display       = 'block';
            saveBtn.style.display        = 'none';
            button.style.display         = 'none';
            editBtn.style.display        = 'inline-block';
        });
    });

    // 저장 버튼 클릭 시: 화면에 즉시 반영 + 서버 업데이트
    document.querySelectorAll('.save-comment-btn').forEach(function(button) {
        button.addEventListener('click', function() {
            const commentItem = button.closest('.comment-item');
            const textSpan    = commentItem.querySelector('.comment-text');
            const editTextarea= commentItem.querySelector('.comment-edit-input');
            const cancelBtn   = commentItem.querySelector('.cancel-edit-btn');
            const editBtn     = commentItem.querySelector('.edit-comment-btn');
            const newText     = editTextarea.value;
            const commentId   = button.getAttribute('data-comment-id');

            // 화면 즉시 반영
            textSpan.textContent       = newText;
            editTextarea.style.display = 'none';
            textSpan.style.display     = 'block';
            button.style.display       = 'none';
            cancelBtn.style.display    = 'none';
            editBtn.style.display      = 'inline-block';

            // 서버에 업데이트 요청
            const parts      = window.location.pathname.split('/');
            const postId     = parts[2], categoryId = parts[3];
            const url        = `/post/${postId}/${categoryId}/comment/update?commentId=${commentId}`;
            const formData   = new FormData();
            formData.append('content', newText);

            fetch(url, {
                method: 'POST',
                body: formData,
                credentials: 'same-origin',
                headers: { [csrfHeader]: csrfToken }
            }).then(res => {
                if (!res.ok) alert('댓글 수정에 실패했습니다.');
            });
        });
    });


// 삭제 버튼
    document.querySelectorAll('.delete-comment-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            if (!confirm('정말 삭제하시겠습니까?')) return;
            const item = btn.closest('.comment-item');
            const commentId = btn.getAttribute('data-comment-id');

            // URL 파싱
            const parts = window.location.pathname.split('/');
            const postId     = parts[2];
            const categoryId = parts[3];

            const url = `/post/${postId}/${categoryId}/comment/delete?commentId=${commentId}`;

            fetch(url, {
                method: 'POST',
                credentials: 'same-origin',
                headers: { [csrfHeader]: csrfToken }
            }).then(res => {
                if (res.ok) item.remove();
                else alert('댓글 삭제에 실패했습니다.');
            });
        });
    });



// Reaction 버튼 이벤트 등록 (게시글 + 댓글)
    document.querySelectorAll('.post-up-btn, .post-down-btn, .comment-up-btn, .comment-down-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var type = btn.classList.contains('post-up-btn') || btn.classList.contains('post-down-btn') ? 'post' : 'comment';
            handleReaction(btn, type);
        });
    });

    function handleReaction(button, type) {
        var box = button.closest(type === 'post' ? '.post-reaction' : '.comment-reaction');
        var memberId = parseInt(box.dataset.memberId);
        if (!box) {
            console.error('Reaction container not found for type:', type);
            return;
        }

        if (!memberId) {
            alert("로그인이 필요한 기능입니다.");
            return;
        }

        var targetId = parseInt(type === 'post' ? box.dataset.postId : box.dataset.commentId);
        var isUp = button.classList.contains(type + '-up-btn');
        var dto = {
            reactionType: isUp?'UP':'DOWN',
            memberDto:{memberId:memberId}
        };
        if (type === 'post') {
            dto.postDto = { postId: targetId };
        } else {
            dto.commentDto = { commentId: targetId};
        }

        fetch('/reactions/' + type, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(dto)
        }).then(res => res.json())
            .then(data => {
                console.log('Sending DTO:', dto);
                console.log('reaction response', data);
                var upCountSpan = box.querySelector('.up-count');
                var downCountSpan = box.querySelector('.down-count');
                var upIcon = box.querySelector('.' + type + '-up-btn i');
                var downIcon = box.querySelector('.' + type + '-down-btn i');

                upCountSpan.textContent = data.UP || 0;
                downCountSpan.textContent = data.DOWN || 0;

                upIcon.classList.remove('fa-thumbs-up','fa-thumbs-o-up');
                downIcon.classList.remove('fa-thumbs-down','fa-thumbs-o-down');

                if (data.userReaction === 'UP') {
                    upIcon.classList.add('fa','fa-thumbs-up');
                    downIcon.classList.add('fa','fa-thumbs-o-down');
                } else if (data.userReaction === 'DOWN') {
                    upIcon.classList.add('fa','fa-thumbs-o-up');
                    downIcon.classList.add('fa','fa-thumbs-down');
                } else {
                    upIcon.classList.add('fa','fa-thumbs-o-up');
                    downIcon.classList.add('fa','fa-thumbs-o-down');
                }
            }).catch(function (error) {
            console.error('Error toggling reaction:', error);
            alert('반응 처리 중 오류가 발생하였습니다.');
        });
    }
});
