import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  Card, 
  CardContent, 
  Button,
  Chip,
  Avatar,
  Divider,
  TextField,
  Alert,
  IconButton,
  Paper
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import { ThumbUp, ThumbDown, Reply, Edit, Delete } from '@mui/icons-material';
import axios from 'axios';

const PostDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newComment, setNewComment] = useState('');
  const [replyTo, setReplyTo] = useState(null);
  const [error, setError] = useState('');
  const [userVote, setUserVote] = useState(null);

  const currentUser = localStorage.getItem('username');
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchPost();
    fetchComments();
  }, [id]);

  const fetchPost = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/posts/${id}`);
      setPost(response.data);
    } catch (error) {
      console.error('게시글을 불러오는데 실패했습니다:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchComments = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/comments/post/${id}`);
      setComments(response.data);
    } catch (error) {
      console.error('댓글을 불러오는데 실패했습니다:', error);
    }
  };

  const handleVote = async (voteType) => {
    if (!token) {
      setError('로그인이 필요합니다.');
      return;
    }

    try {
      const response = await axios.post(`http://localhost:8080/api/votes`, {
        postId: parseInt(id),
        voteType: voteType
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });

      if (response.data) {
        setUserVote(voteType);
        fetchPost(); // 게시글 정보 새로고침
      } else {
        setUserVote(null);
        fetchPost();
      }
    } catch (error) {
      setError('투표에 실패했습니다.');
    }
  };

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!token) {
      setError('로그인이 필요합니다.');
      return;
    }

    try {
      const commentData = {
        content: newComment,
        postId: parseInt(id),
        parentId: replyTo?.id || null,
        isSpoiler: false
      };

      await axios.post('http://localhost:8080/api/comments', commentData, {
        headers: { Authorization: `Bearer ${token}` }
      });

      setNewComment('');
      setReplyTo(null);
      fetchComments();
    } catch (error) {
      setError('댓글 작성에 실패했습니다.');
    }
  };

  const handleDeletePost = async () => {
    if (!window.confirm('정말로 이 게시글을 삭제하시겠습니까?')) return;

    try {
      await axios.delete(`http://localhost:8080/api/posts/${id}?userId=${post.user.id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      navigate('/posts');
    } catch (error) {
      setError('게시글 삭제에 실패했습니다.');
    }
  };

  const getPostTypeColor = (type) => {
    switch (type) {
      case 'REVIEW': return 'primary';
      case 'DISCUSSION': return 'secondary';
      case 'QUESTION': return 'warning';
      case 'NEWS': return 'info';
      case 'GENERAL': return 'default';
      default: return 'default';
    }
  };

  const getPostTypeText = (type) => {
    switch (type) {
      case 'REVIEW': return '후기';
      case 'DISCUSSION': return '토론';
      case 'QUESTION': return '질문';
      case 'NEWS': return '소식';
      case 'GENERAL': return '일반';
      default: return type;
    }
  };

  if (loading) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ mt: 4, textAlign: 'center' }}>
          <Typography>로딩 중...</Typography>
        </Box>
      </Container>
    );
  }

  if (!post) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ mt: 4, textAlign: 'center' }}>
          <Typography>게시글을 찾을 수 없습니다.</Typography>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        {/* 게시글 내용 */}
        <Card sx={{ mb: 4 }}>
          <CardContent>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
              <Box sx={{ flex: 1 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                  {post.title}
                  {post.isSpoiler && (
                    <Chip 
                      label="스포일러" 
                      color="warning" 
                      size="small" 
                      sx={{ ml: 1 }}
                    />
                  )}
                </Typography>
                
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
                  <Chip 
                    label={getPostTypeText(post.postType)} 
                    color={getPostTypeColor(post.postType)}
                  />
                  <Typography variant="body2" color="text.secondary">
                    {post.performance?.title}
                  </Typography>
                </Box>
              </Box>
              
              {currentUser === post.user.username && (
                <Box sx={{ display: 'flex', gap: 1 }}>
                  <IconButton onClick={() => navigate(`/posts/${id}/edit`)}>
                    <Edit />
                  </IconButton>
                  <IconButton onClick={handleDeletePost} color="error">
                    <Delete />
                  </IconButton>
                </Box>
              )}
            </Box>
            
            <Typography variant="body1" paragraph sx={{ whiteSpace: 'pre-wrap' }}>
              {post.content}
            </Typography>
            
            <Divider sx={{ my: 2 }} />
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Avatar>
                  {post.user.nickname?.[0] || post.user.username[0]}
                </Avatar>
                <Box>
                  <Typography variant="body2">
                    {post.user.nickname || post.user.username}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {new Date(post.createdAt).toLocaleString()}
                  </Typography>
                </Box>
              </Box>
              
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button
                  startIcon={<ThumbUp />}
                  variant={userVote === 'LIKE' ? 'contained' : 'outlined'}
                  onClick={() => handleVote('LIKE')}
                >
                  👍 {post.likeCount}
                </Button>
                <Button
                  startIcon={<ThumbDown />}
                  variant={userVote === 'DISLIKE' ? 'contained' : 'outlined'}
                  onClick={() => handleVote('DISLIKE')}
                >
                  👎 {post.dislikeCount}
                </Button>
              </Box>
            </Box>
          </CardContent>
        </Card>

        {/* 댓글 작성 */}
        {token && (
          <Card sx={{ mb: 4 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                댓글 작성
              </Typography>
              
              {replyTo && (
                <Alert severity="info" sx={{ mb: 2 }}>
                  {replyTo.user.nickname || replyTo.user.username}님에게 답글을 작성합니다.
                  <Button size="small" onClick={() => setReplyTo(null)} sx={{ ml: 1 }}>
                    취소
                  </Button>
                </Alert>
              )}
              
              <Box component="form" onSubmit={handleCommentSubmit}>
                <TextField
                  fullWidth
                  multiline
                  rows={3}
                  placeholder="댓글을 작성하세요..."
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                  sx={{ mb: 2 }}
                />
                <Button type="submit" variant="contained" disabled={!newComment.trim()}>
                  댓글 작성
                </Button>
              </Box>
            </CardContent>
          </Card>
        )}

        {/* 댓글 목록 */}
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              댓글 ({comments.length})
            </Typography>
            
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}
            
            {comments.map((comment) => (
              <Box key={comment.id} sx={{ mb: 2, pl: comment.depth * 2 }}>
                <Paper sx={{ p: 2 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <Avatar sx={{ width: 24, height: 24 }}>
                        {comment.user.nickname?.[0] || comment.user.username[0]}
                      </Avatar>
                      <Typography variant="body2" fontWeight="bold">
                        {comment.user.nickname || comment.user.username}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {new Date(comment.createdAt).toLocaleString()}
                      </Typography>
                      {comment.isSpoiler && (
                        <Chip label="스포일러" color="warning" size="small" />
                      )}
                    </Box>
                    
                    {token && (
                      <IconButton size="small" onClick={() => setReplyTo(comment)}>
                        <Reply />
                      </IconButton>
                    )}
                  </Box>
                  
                  <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                    {comment.content}
                  </Typography>
                  
                  {comment.replies && comment.replies.length > 0 && (
                    <Box sx={{ mt: 2 }}>
                      {comment.replies.map((reply) => (
                        <Box key={reply.id} sx={{ mb: 1, pl: 2, borderLeft: '2px solid #e0e0e0' }}>
                          <Paper sx={{ p: 2 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                              <Avatar sx={{ width: 20, height: 20 }}>
                                {reply.user.nickname?.[0] || reply.user.username[0]}
                              </Avatar>
                              <Typography variant="body2" fontWeight="bold">
                                {reply.user.nickname || reply.user.username}
                              </Typography>
                              <Typography variant="caption" color="text.secondary">
                                {new Date(reply.createdAt).toLocaleString()}
                              </Typography>
                              {reply.isSpoiler && (
                                <Chip label="스포일러" color="warning" size="small" />
                              )}
                            </Box>
                            <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                              {reply.content}
                            </Typography>
                          </Paper>
                        </Box>
                      ))}
                    </Box>
                  )}
                </Paper>
              </Box>
            ))}
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
};

export default PostDetail;
