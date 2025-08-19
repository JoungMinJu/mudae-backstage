import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  Grid, 
  Card, 
  CardContent, 
  Button,
  Chip,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Pagination,
  Avatar
} from '@mui/material';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';

const PostList = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [postTypeFilter, setPostTypeFilter] = useState('ALL');
  const [spoilerFilter, setSpoilerFilter] = useState('ALL');
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const performanceId = searchParams.get('performanceId');

  useEffect(() => {
    fetchPosts();
  }, [page, postTypeFilter, spoilerFilter, searchKeyword, performanceId]);

  const fetchPosts = async () => {
    try {
      setLoading(true);
      let url = `http://localhost:8080/api/posts?page=${page - 1}&size=12`;
      
      if (performanceId) {
        url = `http://localhost:8080/api/posts/performance/${performanceId}?page=${page - 1}&size=12`;
      }
      
      if (postTypeFilter !== 'ALL') {
        url = `http://localhost:8080/api/posts/performance/${performanceId || 'all'}/type/${postTypeFilter.toLowerCase()}?page=${page - 1}&size=12`;
      }
      
      if (spoilerFilter !== 'ALL') {
        url = `http://localhost:8080/api/posts/performance/${performanceId || 'all'}/spoiler/${spoilerFilter}?page=${page - 1}&size=12`;
      }
      
      if (searchKeyword) {
        url = `http://localhost:8080/api/posts/search?keyword=${encodeURIComponent(searchKeyword)}&page=${page - 1}&size=12`;
      }
      
      const response = await axios.get(url);
      setPosts(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('게시글 목록을 불러오는데 실패했습니다:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1);
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

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
          <Typography variant="h4" component="h1">
            게시판
          </Typography>
          
          <Button 
            variant="contained" 
            onClick={() => navigate('/posts/create')}
            disabled={!localStorage.getItem('token')}
          >
            글쓰기
          </Button>
        </Box>
        
        <Box sx={{ mb: 4 }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} md={4}>
              <Box component="form" onSubmit={handleSearch} sx={{ display: 'flex', gap: 1 }}>
                <TextField
                  fullWidth
                  placeholder="제목 또는 내용으로 검색..."
                  value={searchKeyword}
                  onChange={(e) => setSearchKeyword(e.target.value)}
                />
                <Button type="submit" variant="contained">
                  검색
                </Button>
              </Box>
            </Grid>
            
            <Grid item xs={12} md={2}>
              <FormControl fullWidth>
                <InputLabel>게시글 타입</InputLabel>
                <Select
                  value={postTypeFilter}
                  label="게시글 타입"
                  onChange={(e) => setPostTypeFilter(e.target.value)}
                >
                  <MenuItem value="ALL">전체</MenuItem>
                  <MenuItem value="REVIEW">후기</MenuItem>
                  <MenuItem value="DISCUSSION">토론</MenuItem>
                  <MenuItem value="QUESTION">질문</MenuItem>
                  <MenuItem value="NEWS">소식</MenuItem>
                  <MenuItem value="GENERAL">일반</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            
            <Grid item xs={12} md={2}>
              <FormControl fullWidth>
                <InputLabel>스포일러</InputLabel>
                <Select
                  value={spoilerFilter}
                  label="스포일러"
                  onChange={(e) => setSpoilerFilter(e.target.value)}
                >
                  <MenuItem value="ALL">전체</MenuItem>
                  <MenuItem value="false">스포일러 없음</MenuItem>
                  <MenuItem value="true">스포일러 있음</MenuItem>
                </Select>
              </FormControl>
            </Grid>
          </Grid>
        </Box>

        <Grid container spacing={3}>
          {posts.map((post) => (
            <Grid item xs={12} key={post.id}>
              <Card 
                sx={{ 
                  cursor: 'pointer',
                  '&:hover': { transform: 'translateY(-2px)', transition: 'transform 0.2s' }
                }}
                onClick={() => navigate(`/posts/${post.id}`)}
              >
                <CardContent>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="h6" component="h2" gutterBottom>
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
                      
                      <Typography variant="body2" color="text.secondary" paragraph noWrap>
                        {post.content}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <Chip 
                        label={getPostTypeText(post.postType)} 
                        color={getPostTypeColor(post.postType)}
                        size="small"
                      />
                    </Box>
                  </Box>
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                      <Avatar sx={{ width: 24, height: 24 }}>
                        {post.user.nickname?.[0] || post.user.username[0]}
                      </Avatar>
                      <Typography variant="body2" color="text.secondary">
                        {post.user.nickname || post.user.username}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {new Date(post.createdAt).toLocaleDateString()}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', gap: 2 }}>
                      <Typography variant="body2" color="text.secondary">
                        👁️ {post.viewCount}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        👍 {post.likeCount}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        💬 {post.commentCount}
                      </Typography>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>

        {totalPages > 1 && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
            <Pagination 
              count={totalPages} 
              page={page} 
              onChange={(e, value) => setPage(value)}
              color="primary"
            />
          </Box>
        )}
      </Box>
    </Container>
  );
};

export default PostList;
