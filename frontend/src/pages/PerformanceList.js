import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  Grid, 
  Card, 
  CardContent, 
  CardMedia, 
  Button,
  Chip,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Pagination
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const PerformanceList = () => {
  const [performances, setPerformances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  useEffect(() => {
    fetchPerformances();
  }, [page, statusFilter, searchKeyword]);

  const fetchPerformances = async () => {
    try {
      setLoading(true);
      let url = `http://localhost:8080/api/performances?page=${page - 1}&size=12`;
      
      if (statusFilter !== 'ALL') {
        url = `http://localhost:8080/api/performances/status/${statusFilter.toLowerCase()}?page=${page - 1}&size=12`;
      }
      
      if (searchKeyword) {
        url = `http://localhost:8080/api/performances/search?keyword=${encodeURIComponent(searchKeyword)}&page=${page - 1}&size=12`;
      }
      
      const response = await axios.get(url);
      setPerformances(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('공연 목록을 불러오는데 실패했습니다:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'UPCOMING': return 'primary';
      case 'ONGOING': return 'success';
      case 'COMPLETED': return 'default';
      case 'CANCELLED': return 'error';
      default: return 'default';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'UPCOMING': return '예정';
      case 'ONGOING': return '진행중';
      case 'COMPLETED': return '종료';
      case 'CANCELLED': return '취소';
      default: return status;
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
        <Typography variant="h4" component="h1" gutterBottom>
          공연 목록
        </Typography>
        
        <Box sx={{ mb: 4 }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} md={6}>
              <Box component="form" onSubmit={handleSearch} sx={{ display: 'flex', gap: 1 }}>
                <TextField
                  fullWidth
                  placeholder="공연 제목으로 검색..."
                  value={searchKeyword}
                  onChange={(e) => setSearchKeyword(e.target.value)}
                />
                <Button type="submit" variant="contained">
                  검색
                </Button>
              </Box>
            </Grid>
            
            <Grid item xs={12} md={3}>
              <FormControl fullWidth>
                <InputLabel>상태</InputLabel>
                <Select
                  value={statusFilter}
                  label="상태"
                  onChange={(e) => setStatusFilter(e.target.value)}
                >
                  <MenuItem value="ALL">전체</MenuItem>
                  <MenuItem value="UPCOMING">예정</MenuItem>
                  <MenuItem value="ONGOING">진행중</MenuItem>
                  <MenuItem value="COMPLETED">종료</MenuItem>
                  <MenuItem value="CANCELLED">취소</MenuItem>
                </Select>
              </FormControl>
            </Grid>
          </Grid>
        </Box>

        <Grid container spacing={3}>
          {performances.map((performance) => (
            <Grid item xs={12} sm={6} md={4} key={performance.id}>
              <Card 
                sx={{ 
                  height: '100%', 
                  cursor: 'pointer',
                  '&:hover': { transform: 'translateY(-4px)', transition: 'transform 0.2s' }
                }}
                onClick={() => navigate(`/performances/${performance.id}`)}
              >
                <CardMedia
                  component="img"
                  height="200"
                  image={performance.posterImage || '/default-poster.jpg'}
                  alt={performance.title}
                  sx={{ objectFit: 'cover' }}
                />
                <CardContent>
                  <Typography variant="h6" component="h2" gutterBottom noWrap>
                    {performance.title}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    {performance.genre} • {performance.venue}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    {performance.startDate} ~ {performance.endDate}
                  </Typography>
                  
                  <Box sx={{ mt: 1 }}>
                    <Chip 
                      label={getStatusText(performance.status)} 
                      color={getStatusColor(performance.status)}
                      size="small"
                    />
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

export default PerformanceList;
