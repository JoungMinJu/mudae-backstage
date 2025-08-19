import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  Grid, 
  Card, 
  CardContent, 
  CardMedia, 
  Chip,
  Button,
  Divider,
  Paper
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const PerformanceDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [performance, setPerformance] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPerformance();
  }, [id]);

  const fetchPerformance = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/api/performances/${id}`);
      setPerformance(response.data);
    } catch (error) {
      console.error('공연 정보를 불러오는데 실패했습니다:', error);
    } finally {
      setLoading(false);
    }
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

  if (!performance) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ mt: 4, textAlign: 'center' }}>
          <Typography>공연을 찾을 수 없습니다.</Typography>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={4}>
          {/* 포스터 이미지 */}
          <Grid item xs={12} md={4}>
            <Card>
              <CardMedia
                component="img"
                height="400"
                image={performance.posterImage || '/default-poster.jpg'}
                alt={performance.title}
                sx={{ objectFit: 'cover' }}
              />
            </Card>
          </Grid>

          {/* 공연 정보 */}
          <Grid item xs={12} md={8}>
            <Box sx={{ mb: 3 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
                <Typography variant="h3" component="h1">
                  {performance.title}
                </Typography>
                <Chip 
                  label={getStatusText(performance.status)} 
                  color={getStatusColor(performance.status)}
                  size="large"
                />
              </Box>

              <Typography variant="h6" color="text.secondary" gutterBottom>
                {performance.genre} • {performance.venue}
              </Typography>

              <Typography variant="body1" paragraph>
                {performance.description}
              </Typography>
            </Box>

            <Paper sx={{ p: 3, mb: 3 }}>
              <Grid container spacing={3}>
                <Grid item xs={6} sm={3}>
                  <Typography variant="subtitle2" color="text.secondary">
                    공연 기간
                  </Typography>
                  <Typography variant="body1">
                    {performance.startDate} ~ {performance.endDate}
                  </Typography>
                </Grid>
                
                <Grid item xs={6} sm={3}>
                  <Typography variant="subtitle2" color="text.secondary">
                    공연 시간
                  </Typography>
                  <Typography variant="body1">
                    {performance.runningTime}분
                  </Typography>
                </Grid>
                
                <Grid item xs={6} sm={3}>
                  <Typography variant="subtitle2" color="text.secondary">
                    장르
                  </Typography>
                  <Typography variant="body1">
                    {performance.genre}
                  </Typography>
                </Grid>
                
                <Grid item xs={6} sm={3}>
                  <Typography variant="subtitle2" color="text.secondary">
                    공연장
                  </Typography>
                  <Typography variant="body1">
                    {performance.venue}
                  </Typography>
                </Grid>
              </Grid>
            </Paper>

            <Box sx={{ display: 'flex', gap: 2 }}>
              <Button 
                variant="contained" 
                size="large"
                onClick={() => navigate(`/posts?performanceId=${id}`)}
              >
                이 공연에 대한 이야기 보기
              </Button>
              
              <Button 
                variant="outlined" 
                size="large"
                onClick={() => navigate('/performances')}
              >
                다른 공연 보기
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

export default PerformanceDetail;
